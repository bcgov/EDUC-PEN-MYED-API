envValue=$1
APP_NAME=$2
PEN_NAMESPACE=$3
COMMON_NAMESPACE=$4
APP_NAME_UPPER=${APP_NAME^^}
TZVALUE="America/Vancouver"
SOAM_KC_REALM_ID="master"
KCADM_FILE_BIN_FOLDER="/tmp/keycloak-9.0.3/bin"
SOAM_KC=soam-$envValue.apps.silver.devops.gov.bc.ca

oc project $COMMON_NAMESPACE-$envValue
SOAM_KC_LOAD_USER_ADMIN=$(oc -o json get secret sso-admin-${envValue} | sed -n 's/.*"username": "\(.*\)"/\1/p' | base64 --decode)
SOAM_KC_LOAD_USER_PASS=$(oc -o json get secret sso-admin-${envValue} | sed -n 's/.*"password": "\(.*\)",/\1/p' | base64 --decode)

##
oc project $PEN_NAMESPACE-$envValue
##DB_JDBC_CONNECT_STRING=$(oc -o json get configmaps ${APP_NAME}-${envValue}-setup-config | sed -n 's/.*"DB_JDBC_CONNECT_STRING": "\(.*\)",/\1/p')
##DB_PWD=$(oc -o json get configmaps ${APP_NAME}-${envValue}-setup-config | sed -n "s/.*\"DB_PWD_${APP_NAME_UPPER}\": \"\(.*\)\",/\1/p")
##DB_USER=$(oc -o json get configmaps "${APP_NAME}"-"${envValue}"-setup-config | sed -n "s/.*\"DB_USER_${APP_NAME_UPPER}\": \"\(.*\)\",/\1/p")
SPLUNK_TOKEN=$(oc -o json get configmaps "${APP_NAME}"-"${envValue}"-setup-config | sed -n "s/.*\"SPLUNK_TOKEN_${APP_NAME_UPPER}\": \"\(.*\)\"/\1/p")
NATS_URL="nats://nats.${COMMON_NAMESPACE}-${envValue}.svc.cluster.local:4222"
oc project $PEN_NAMESPACE-tools

$KCADM_FILE_BIN_FOLDER/kcadm.sh config credentials --server https://$SOAM_KC/auth --realm $SOAM_KC_REALM_ID --user $SOAM_KC_LOAD_USER_ADMIN --password $SOAM_KC_LOAD_USER_PASS

getPenMyEdClientID() {
  executorID= $KCADM_FILE_BIN_FOLDER/kcadm.sh get clients -r $SOAM_KC_REALM_ID --fields 'id,clientId' | grep -B2 '"clientId" : "pen-myed-api-service"' | grep -Po "(\{){0,1}[0-9a-fA-F]{8}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{12}(\}){0,1}"
}
PME_CLIENT_ID=$(getPenMyEdClientID)

echo Removing PEN CONSUMER client if exists
$KCADM_FILE_BIN_FOLDER/kcadm.sh delete clients/"$PME_CLIENT_ID" -r $SOAM_KC_REALM_ID

echo Creating PEN CONSUMER API Keycloak client
$KCADM_FILE_BIN_FOLDER/kcadm.sh create clients -r $SOAM_KC_REALM_ID --body "{\"clientId\" : \"pen-myed-api-service\",\"surrogateAuthRequired\" : false,\"enabled\" : true,\"clientAuthenticatorType\" : \"client-secret\",\"redirectUris\" : [ ],\"webOrigins\" : [ ],\"notBefore\" : 0,\"bearerOnly\" : false,\"consentRequired\" : false,\"standardFlowEnabled\" : false,\"implicitFlowEnabled\" : false,\"directAccessGrantsEnabled\" : false,\"serviceAccountsEnabled\" : true,\"publicClient\" : false,\"frontchannelLogout\" : false,\"protocol\" : \"openid-connect\",\"attributes\" : {\"saml.assertion.signature\" : \"false\",\"saml.multivalued.roles\" : \"false\",\"saml.force.post.binding\" : \"false\",\"saml.encrypt\" : \"false\",\"saml.server.signature\" : \"false\",\"saml.server.signature.keyinfo.ext\" : \"false\",\"exclude.session.state.from.auth.response\" : \"false\",\"saml_force_name_id_format\" : \"false\",\"saml.client.signature\" : \"false\",\"tls.client.certificate.bound.access.tokens\" : \"false\",\"saml.authnstatement\" : \"false\",\"display.on.consent.screen\" : \"false\",\"saml.onetimeuse.condition\" : \"false\"},\"authenticationFlowBindingOverrides\" : { },\"fullScopeAllowed\" : true,\"nodeReRegistrationTimeout\" : -1,\"protocolMappers\" : [ {\"name\" : \"Client ID\",\"protocol\" : \"openid-connect\",\"protocolMapper\" : \"oidc-usersessionmodel-note-mapper\",\"consentRequired\" : false,\"config\" : {\"user.session.note\" : \"clientId\",\"id.token.claim\" : \"true\",\"access.token.claim\" : \"true\",\"claim.name\" : \"clientId\",\"jsonType.label\" : \"String\"}}, {\"name\" : \"Client Host\",\"protocol\" : \"openid-connect\",\"protocolMapper\" : \"oidc-usersessionmodel-note-mapper\",\"consentRequired\" : false,\"config\" : {\"user.session.note\" : \"clientHost\",\"id.token.claim\" : \"true\",\"access.token.claim\" : \"true\",\"claim.name\" : \"clientHost\",\"jsonType.label\" : \"String\"}}, {\"name\" : \"Client IP Address\",\"protocol\" : \"openid-connect\",\"protocolMapper\" : \"oidc-usersessionmodel-note-mapper\",\"consentRequired\" : false,\"config\" : {\"user.session.note\" : \"clientAddress\",\"id.token.claim\" : \"true\",\"access.token.claim\" : \"true\",\"claim.name\" : \"clientAddress\",\"jsonType.label\" : \"String\"}} ],\"defaultClientScopes\" : [ \"web-origins\",\"WRITE_PEN_REQUEST_BATCH\",\"READ_PEN_REQUEST_BATCH\",\"READ_SCHOOL\", \"READ_PEN_COORDINATOR\",\"VALIDATE_STUDENT_DEMOGRAPHICS\", \"READ_PEN_MATCH\",\"WRITE_STUDENT\",\"READ_STUDENT\",\"role_list\", \"profile\", \"roles\", \"email\"],\"optionalClientScopes\" : [ \"address\", \"phone\", \"offline_access\" ],\"access\" : {\"view\" : true,\"configure\" : true,\"manage\" : true}}"

getPMEAPIServiceClientSecret() {
  executorID= $KCADM_FILE_BIN_FOLDER/kcadm.sh get clients/"$PME_APIServiceClientID"/client-secret -r $SOAM_KC_REALM_ID | grep -Po "(\{){0,1}[0-9a-fA-F]{8}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{12}(\}){0,1}"
}

PME_APIServiceClientID=$(getPenMyEdClientID)
PME_APIServiceClientSecret=$(getPMEAPIServiceClientSecret)
#MYED_WRITE_PEN_REQUEST_BATCH
$KCADM_FILE_BIN_FOLDER/kcadm.sh create client-scopes -r $SOAM_KC_REALM_ID --body "{\"description\": \"write pen request batch scope for myed\",\"id\": \"MYED_WRITE_PEN_REQUEST_BATCH\",\"name\": \"MYED_WRITE_PEN_REQUEST_BATCH\",\"protocol\": \"openid-connect\",\"attributes\" : {\"include.in.token.scope\" : \"true\",\"display.on.consent.screen\" : \"false\"}}"

#MYED_READ_PEN_REQUEST_BATCH
$KCADM_FILE_BIN_FOLDER/kcadm.sh create client-scopes -r $SOAM_KC_REALM_ID --body "{\"description\": \"Read scope for MyEd\",\"id\": \"MYED_READ_PEN_REQUEST_BATCH\",\"name\": \"MYED_READ_PEN_REQUEST_BATCH\",\"protocol\": \"openid-connect\",\"attributes\" : {\"include.in.token.scope\" : \"true\",\"display.on.consent.screen\" : \"false\"}}"

#SCOPE_MYED_READ_PEN_COORDINATOR
$KCADM_FILE_BIN_FOLDER/kcadm.sh create client-scopes -r $SOAM_KC_REALM_ID --body "{\"description\": \"Read pen coordinator scope for MyEd\",\"id\": \"MYED_READ_PEN_COORDINATOR\",\"name\": \"MYED_READ_PEN_COORDINATOR\",\"protocol\": \"openid-connect\",\"attributes\" : {\"include.in.token.scope\" : \"true\",\"display.on.consent.screen\" : \"false\"}}"

#MYED_VALIDATE_PEN
$KCADM_FILE_BIN_FOLDER/kcadm.sh create client-scopes -r $SOAM_KC_REALM_ID --body "{\"description\": \"Validate pen scope for MyEd\",\"id\": \"MYED_VALIDATE_PEN\",\"name\": \"MYED_VALIDATE_PEN\",\"protocol\": \"openid-connect\",\"attributes\" : {\"include.in.token.scope\" : \"true\",\"display.on.consent.screen\" : \"false\"}}"

#MYED_PEN_REQUEST
$KCADM_FILE_BIN_FOLDER/kcadm.sh create client-scopes -r $SOAM_KC_REALM_ID --body "{\"description\": \"one of pen request scope for MyEd\",\"id\": \"MYED_PEN_REQUEST\",\"name\": \"MYED_PEN_REQUEST\",\"protocol\": \"openid-connect\",\"attributes\" : {\"include.in.token.scope\" : \"true\",\"display.on.consent.screen\" : \"false\"}}"

#MYED_READ_STUDENT
$KCADM_FILE_BIN_FOLDER/kcadm.sh create client-scopes -r $SOAM_KC_REALM_ID --body "{\"description\": \"Read student details scope for MyEd\",\"id\": \"MYED_READ_STUDENT\",\"name\": \"MYED_READ_STUDENT\",\"protocol\": \"openid-connect\",\"attributes\" : {\"include.in.token.scope\" : \"true\",\"display.on.consent.screen\" : \"false\"}}"

###########################################################
#Setup for config-map
###########################################################
SPLUNK_URL="gww.splunk.educ.gov.bc.ca"
FLB_CONFIG="[SERVICE]
   Flush        1
   Daemon       Off
   Log_Level    debug
   HTTP_Server   On
   HTTP_Listen   0.0.0.0
   HTTP_Port     2020
[INPUT]
   Name   tail
   Path   /mnt/log/*
   Mem_Buf_Limit 20MB
[FILTER]
   Name record_modifier
   Match *
   Record hostname \"\${HOSTNAME}\",
[OUTPUT]
   Name   stdout
   Match  *
[OUTPUT]
   Name  splunk
   Match *
   Host  $SPLUNK_URL
   Port  443
   TLS         On
   TLS.Verify  Off
   Message_Key $APP_NAME
   Splunk_Token $SPLUNK_TOKEN
"

echo Creating config map "$APP_NAME"-config-map
oc create -n "$PEN_NAMESPACE"-"$envValue" configmap "$APP_NAME"-config-map --from-literal=TZ=$TZVALUE --from-literal=STUDENT_API_URL="http://student-api-master.$COMMON_NAMESPACE-$envValue.svc.cluster.local:8080/api/v1/student" --from-literal=SCHOOL_API_URL="http://school-api-master.$COMMON_NAMESPACE-$envValue.svc.cluster.local:8080/api/v1/schools" --from-literal=PEN_REG_BATCH_API_URL="http://pen-reg-batch-api-master.$PEN_NAMESPACE-$envValue.svc.cluster.local:8080/api/v1/pen-request-batch" --from-literal=PEN_MATCH_API_URL="http://pen-match-api-master.$PEN_NAMESPACE-$envValue.svc.cluster.local:8080/api/v1/pen-match" --from-literal=PEN_SERVICES_API_URL="http://pen-services-api-master.$PEN_NAMESPACE-$envValue.svc.cluster.local:8080/api/v1/pen-services" --from-literal=TOKEN_URL="https://$SOAM_KC/auth/realms/$SOAM_KC_REALM_ID/protocol/openid-connect/token" --from-literal=SPRING_SECURITY_LOG_LEVEL=INFO --from-literal=SPRING_WEB_LOG_LEVEL=INFO --from-literal=APP_LOG_LEVEL=INFO --from-literal=SPRING_BOOT_AUTOCONFIG_LOG_LEVEL=INFO --from-literal=SPRING_SHOW_REQUEST_DETAILS=false --from-literal=NATS_URL="$NATS_URL" --from-literal=TOKEN_ISSUER_URL="https://$SOAM_KC/auth/realms/$SOAM_KC_REALM_ID" --from-literal=CLIENT_ID="pen-myed-api-service" --from-literal=CLIENT_SECRET="$PME_APIServiceClientSecret" --dry-run -o yaml | oc apply -f -

echo
echo Setting environment variables for "$APP_NAME-$SOAM_KC_REALM_ID" application
oc project "$PEN_NAMESPACE-$envValue"
oc set env --from=configmap/"$APP_NAME"-config-map dc/"$APP_NAME"-main

echo Creating config map "$APP_NAME"-flb-sc-config-map
oc create -n "$PEN_NAMESPACE"-"$envValue" configmap "$APP_NAME"-flb-sc-config-map --from-literal=fluent-bit.conf="$FLB_CONFIG" --dry-run -o yaml | oc apply -f -
