FROM alpine:3.9

# docker build -t alpine-jdk:8u211 .
# docker run alpine-jdk:8u211
MAINTAINER Jiang Chuan <jctr@qq.com>

# copy file
ADD jdk-8u211-linux-x64.tar.gz /usr/java/

# Java Version and other ENV
ENV JAVA_HOME=/usr/java/default \
    CLASSPATH=/usr/java/default/bin \
    PATH=${PATH}:/usr/java/default/bin \
    GLIBC_REPO=https://github.com/sgerrand/alpine-pkg-glibc \
    GLIBC_VERSION=2.29-r0 \
    LANG=C.UTF-8

# do all in one step
RUN echo http://mirrors.aliyun.com/alpine/v3.9/main/ > /etc/apk/repositories && \
    echo http://mirrors.aliyun.com/alpine/v3.9/community/ >> /etc/apk/repositories && \
    apk update && apk upgrade  && \
    apk --no-cache add libstdc++ curl ca-certificates bash java-cacerts && \
    for pkg in glibc-${GLIBC_VERSION} glibc-bin-${GLIBC_VERSION} ; do curl -sSL ${GLIBC_REPO}/releases/download/${GLIBC_VERSION}/${pkg}.apk -o /tmp/${pkg}.apk; done && \
    apk add --allow-untrusted /tmp/*.apk && \
    rm -v /tmp/*.apk && \
    echo "export LANG=C.UTF-8" > /etc/profile.d/locale.sh && \
    /usr/glibc-compat/sbin/ldconfig /lib /usr/glibc-compat/lib && \
    cd /usr/java/ && ln -s jdk1.8.0_211 default

CMD ["java","-version"]
