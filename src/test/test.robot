# ###############################################################################################
# ############################## AUTOMAÇÃO TESTE EBSERH PACIENTES ###############################
# ###############################################################################################

#-----------------------------------------------------------------------------------------------
# INSTALAÇÃO:
#   pip install robotframework
#   pip install robotframework-extendedrequestslibrary
#   pip install robotframework-jsonlibrary
#   pip install robotframework-seleniumlibrary
# UTILIZAÇÃO: ..\Python\Python310\Scripts\robot test.robot
#-----------------------------------------------------------------------------------------------


*** Settings ***
Library  OperatingSystem
Library  Collections
Library  RequestsLibrary
Library  JSONLibrary

*** Variable ***
${HOST_TESTE}  https://pacientes.apps.nprd.ebserh
#${HOST_TESTE}  http://localhost:8080

${SSO_HOST}  https://login.des.ebserh/auth/realms/intranet/protocol/openid-connect
${SSO_TOKEN_NAME}  KEY_EBSERH
${SSO_CLIENT_ID}  cli-web-ebserh
${SSO_CLIENT_SECRET}   
${SSO_SCOPE}  openid
${SSO_GRANT_TYPE}  password


${arquivojson}  NONE
${sso_usuario}  NONE
${sso_senha}  NONE
${sso_access_token}  NONE
${headersAPI}  NONE


*** Test Cases ***
Teste Case JSON e SSO
    carregarjson  test.json
	#tokenSSO

Teste Case Verificacao
	TesteDeVerificacao
	
*** Keywords ***
#------------------------------- Arquivo JSON ------------------------------
carregarjson
	[Arguments]  ${argArquivoJson}
	Log To Console  \n-> Carregando arquivo "${argArquivoJson}" ...\n
	${varArquivojson}=  Load Json From File  ${argArquivoJson}
	Set Global Variable    ${arquivojson}  ${varArquivojson}
	${usuariojson}=  Get Value From Json  ${arquivojson}  usuario
	Set Global Variable  ${sso_usuario}  ${usuariojson}[0]
	${senhajson}=  Get Value From Json  ${arquivojson}  senha
	Set Global Variable  ${sso_senha}  ${senhajson}[0]

	
tokenSSO
	RequestsLibrary.Create Session  sessaoSSO  ${SSO_HOST}  verify=false
	${sso_data}=  Create Dictionary  Token_Name=${SSO_TOKEN_NAME}  client_id=${SSO_CLIENT_ID}  Client_Secret=${SSO_CLIENT_SECRET}  username=${sso_usuario}  password=${sso_senha}  scope=${SSO_SCOPE}  grant_type=${SSO_GRANT_TYPE}
	Log To Console  \n\n-> POST: ${SSO_HOST}\n
	Log to Console  ${sso_data}
	${sso_headers}=  Create Dictionary  Content-Type=application/x-www-form-urlencoded
	${resp}=  POST On Session  sessaoSSO  /token  data=${sso_data}  headers=${sso_headers}
	Should Be Equal As Strings  ${resp.status_code}  200
	#${accessToken}=  evaluate  $resp.json().get("access_token")
	Log to Console  \n--------------- ACCESTOKEN INI --------------- 
	Log to Console  \n${resp.json().get("access_token")}
	Log to Console  \n--------------- ACCESTOKEN FIM --------------- 
	Set Global Variable  ${sso_access_token}  ${resp.json().get("access_token")}
    ${headers}  Create Dictionary  Content-Type=application/json  apikey=${API_KEY}  Authorization=Bearer ${sso_access_token}
    Set Global Variable  ${headersAPI}  ${headers}
    Log To Console  ${headersAPI}

	
TesteDeVerificacao
	#------------------------------- Arquivo JSON ------------------------------
	Log To Console  Lendo JSON...
	${testejson}=  Load Json From File  test.json
	${parametro}=  Get Value From Json  ${testejson}  parametro
	Log To Console  ${parametro}[0]
	#------------------------------- Chamando API ------------------------------
	Log To Console  Conectando...
    Create Session  sessao  ${HOST_TESTE}  verify=false
    #${response}=  GET On Session  sessao  /v1/teste  params=parametro=teste
	${response}=  GET On Session  sessao  /api/teste
    Status Should Be  200  ${response}  #Verifica Status 200
	${responseContent}=  Convert To String  ${response.content}
	Log To Console  \n${responseContent}

	
TesteCaseExemplo
    ${paramjson}=  Get Value From Json  ${arquivojson}  testecase
	${cntjson}=  Get Length  ${paramjson}
	IF  ${cntjson} > 0
	
	    ${contentjson}=  Set Variable  ${paramjson}[0]
		${cpf}=  Set Variable  ${contentjson['cpf']}
		${ispb}=  Set Variable  ${contentjson['ispb']}
		#${dados}=  Set Variable  ${contentjson['dados']}
		${dados}=  evaluate  json.dumps(${contentjson['dados']})  json
		Log To Console  \n\n-> DADOS: ${dados}\n
		
		${url}=  Set Variable  /api/in/${ispb}/msgs
		${urlEndpoint}=  Set Variable  ${HOST_TESTE}${url}
		Log To Console  \n\n-> POST: ${urlEndpoint}\n
		
	    Create Session  sessao  ${HOST_TESTE}  verify=false
		#${response}=  Run Keyword And Ignore Error  POST On Session  sessao  url=${url}  data=${dados}  headers=${headersAPI}
		${response}=  POST On Session  sessao  url=${url}  data=${dados}  headers=${headersAPI}
        ${responseContent}=  Convert To String  ${response.content}
		
		Log To Console  \n---------- RESPONSE INI -------------
        Log To Console  \n${responseContent}
		Log To Console  \n---------- RESPONSE FIM -------------
		
	ELSE
        Fail  SEM PARAMETROS PARA O ENDPOINT
	END
	

	
