FROM archlinux:latest
EXPOSE 80
EXPOSE 443
USER root
RUN ["pacman","--noconfirm","-Syu","jdk-openjdk","maven"]
VOLUME [ "/data" ]
WORKDIR /data
# ENV JAVA_HOME=
CMD ["echo \"$(find / | grep ^/usr/lib/jvm/java-..-openjdk$)\""]
CMD [ "mvn","-B","package","--file","pom.xml" ]
