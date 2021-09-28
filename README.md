
> # [![](https://raw.githubusercontent.com/TaYaKi71751/NVMe/gh-pages/svg/n.svg)](https://github.com/TaYaKi71751/NVMe/releases)
> 
[![](https://github.com/TaYaKi71751/NVMe/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/TaYaKi71751/NVMe/actions/workflows/codeql-analysis.yml)
[![](https://github.com/TaYaKi71751/NVme/actions/workflows/main.yml/badge.svg)](https://github.com/TaYaKi71751/NVme/actions/workflows/main.yml)
> [![License: WTFPL](https://img.shields.io/badge/License-WTFPL-brightgreen.svg)](http://www.wtfpl.net/about/)
> <br>
> ## Ref
>> [changdoc/naver-vaccine-macro](https://github.com/changdoc/naver-vaccine-macro)<br>
>> [SJang1/korea-covid-19-remaining-vaccine-macro](https://github.com/SJang1/korea-covid-19-remaining-vaccine-macro)<br>
> ## Install Dependencies
>> ```
>> pacman -S jdk-openjdk maven \
>>           firefox-developer-edition
>> ```
> ## Config
>> ```
>> //TODO 
>> DECLARE:
>> FirefoxDeveloperEdition f;
>> NaverSignIn n;
>> START:
>> f = new FirefoxDeveloperEdition(){{
>>    developerMenu(true);
>>    secretMode(false);
>> }};
>> SIGNIN:
>> n = new NaverSignIn(){{
>>    id = ${{ secrets.naver.id }};
>>    pw = ${{ secrets.naver.pw }};
>>    log.keepon(true);
>> }}.then(() -> {
>>    submit();
>> }).onSuccess((cookies) -> {
>>    f.setCookies(cookies);
>>    f.onRedirect((req,res) -> {
>>        if(res.headers.get("Location").contains("nidlogin.login")
>>            && (req.method() != "POST"){
>>            goto SIGNIN;
>>        }
>>    });
>>    f.onSuccess((res) -> {
>>        if(res.statusCode() == 200
>>          && res.url().contains("auth?key")){
>>            doAuth(); //Auth(N.C ?? P.N);
>>        }
>>    });
>>    f.get(reservation_url).then(() -> {
>>        if(f.response.code = 200 
>>            && f.response.url().contains("info?key")){
>>              f.developerMenu.networkTab.clear();
>>              f.get(reservation_url).then(() -> {
>>                  if(f.response.code = 200 && f.response.url().contains("info?key")){
>>                      f.developerMenu.networkTab.saveAllAsHar();
>>                  }
>>                });
>>        }else{
>>            goto START;
>>        }
>>    });
>> });
>> ```
> ## Run
>> <pre>
>> <code>
>> java -jar $(JAR_FILE_NAME).jar
>> </code>
>> </pre>
<!-- reservation -> auth -> info  -->
