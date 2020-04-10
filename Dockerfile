FROM debian:buster

ENV CANTALOUPE_VERSION=4.1.5

EXPOSE 8182

VOLUME /imageroot

# Update packages and install tools
RUN apt-get update -qy && apt-get dist-upgrade -qy && \
    apt-get install -qy --no-install-recommends curl imagemagick \
    libopenjp2-tools ffmpeg unzip default-jre-headless && \
    apt-get -qqy autoremove && apt-get -qqy autoclean

# Run non privileged
RUN adduser --system cantaloupe

# Get and unpack Cantaloupe release archive
COPY cantaloupe-$CANTALOUPE_VERSION.war /cantaloupe/cantaloupe-$CANTALOUPE_VERSION.war 
COPY deps /cantaloupe/deps

RUN mkdir -p /var/log/cantaloupe /var/cache/cantaloupe \
    && chown -R cantaloupe /cantaloupe /var/log/cantaloupe /var/cache/cantaloupe \
    && cp -rs /cantaloupe/deps/Linux-x86-64/* /usr/

COPY delegates.rb cantaloupe
COPY cantaloupe.properties cantaloupe
USER cantaloupe
CMD ["sh", "-c", "java -Dcantaloupe.config=/cantaloupe/cantaloupe.properties -jar /cantaloupe/cantaloupe-$CANTALOUPE_VERSION.war"]
