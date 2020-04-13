
# 🍈 Cantaloupe - Dokcer image build

*High-performance dynamic image server in Java*

# TODO
* Get source bucket name from ENV
* Remove any unneeded environment variables

# Build an image

```
docker image build -t registryaccount/name:tag .
```

# Environment variables

## AWS key

Set both `AWS_ACCESS_KEY_ID` and `AWS_SECRET_KEY` environment variables.

## Other

Some of these are no longer needed.

```
HTTP_HTTP2_ENABLED=true
ENDPOINT_IIIF_CONTENT_DISPOSITION=none 
LOG_APPLICATION_LEVEL=warn 
S3CACHE_BUCKET_NAME=yale-image-samples
S3_SOURCE_BUCKET_NAME=yale-image-samples
S3SOURCE_BASICLOOKUPSTRATEGY_BUCKET_NAME=yale-image-samples
S3SOURCE_BASICLOOKUPSTRATEGY_PATH_PREFIX=ptiffs/
```

# License

Cantaloupe is open-source software distributed under the University of
Illinois/NCSA Open Source License; see the file LICENSE.txt for terms.
