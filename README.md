
> # [![](https://raw.githubusercontent.com/TaYaKi71751/NVMe/gh-pages/svg/n.svg)](https://github.com/TaYaKi71751/NVMe/releases)
> [![](https://github.com/TaYaKi71751/NVme/actions/workflows/main.yml/badge.svg)](https://github.com/TaYaKi71751/NVme/actions/workflows/main.yml)
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
>> LOOP:
>> FirefoxDeveloperEdition f = new FirefoxDeveloperEdition(){{
>>    developerMenu(true);
>>    secretMode(false);
>> }};
>> NaverSignIn n = new NaverSignIn(){{
>>    id = ${{ secrets.naver.id }};
>>    pw = ${{ secrets.naver.pw }};
>>    log.keepon(true);
>> }}.then(() -> {
>>    submit();
>> }).onSuccess((cookies) -> {
>>    f.setCookies(cookies);
>>    f.onRedirect((r) -> {
>>        if(r.headers.get("Location").contains("nidlogin.login?")){
>>            n.doLogin();
>>            f.setCookies(n.getCookies());
>>        }
>>    });
>>    f.onSuccess((res) -> {
>>        if(res.statusCode() == 200
>>          && res.url().contains("auth?key")){
>>            doAuth();
>>        }
>>    });
>>    f.get(reservation_url);
>>    
>>    if(f.response.code = 200 && f.response.url().contains("info?key")){
>>        f.developerMenu.networkTab.clear();
>>        f.get(reservation_url);
>>        f.developerMenu.networkTab.saveAllAsHar(new java.io.File($SAME_DIRECTORY_WHERE_JAR_FILE).getPath() + "/" + $HAR_FILE_NAME) + ".har");
>>    }else{
>>        goto LOOP;
>>    }
>> });
>> ```
> ## Run
>> <pre>
>> <code>
>> java -jar $(JAR_FILE_NAME).jar
>> </code>
>> </pre>
<!-- reservation -> auth -> info  -->