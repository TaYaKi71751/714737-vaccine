> # NVMe(PCIe)
> [![](https://github.com/TaYaKi71751/NVme/actions/workflows/main.yml/badge.svg)](https://github.com/TaYaKi71751/NVme/actions/workflows/main.yml)
> <br>
> Naver Vcc Mcr xec
> ## Install Dependencies
>> ```
>> pacman -S jdk-openjdk maven \
>>           firefox-developer-edition
>> ```
> ## Config
>> ```
>> //TODO 
>> new Firefox(popcat_url).asSecretMode(true).withDeveloperMenu(true).execute().then(this -> {
>>   this.pop(cat).then((void) ->{
>>     Tab networkTab = this.developerMenu.networkTab;
>>     networkTab->saveAllAsHar(new java.io.File($SAME_DIRECTORY_WHERE_JAR_FILE).getPath() 
>>          + "/" + $HAR_FILE_NAME) + ".har");
>>   }); 
>>  });
>> ```
> ## Run
>> <pre>
>> <code>
>> java -jar $(JAR_FILE_NAME).jar
>> </code>
>> </pre>
