title: Uploading Big Files with Micronaut and Elastic Beanstalk
date: February 21, 2019 
description: If you want to upload files with Micronaut, you may need to configure maximum request size and the max file size properties. Here's how to do that.
author: Sergio Del Amo Caballero
image: 2019-02-21.png
---

# [%title]

**By [%author]**

[%date] 

If you want to [upload files](https://docs.micronaut.io/1.1.0.M1/guide/index.html#uploads) with Micronaut, you may need to configure maximum request size and the max file size properties:

Here's how to do that.

```
micronaut:
    server:
       max-request-size: '100MB'
       multipart:
           max-file-size: '100MB'
```

If you wish to to deploy [AWS Elastic Beanstalk](https://aws.amazon.com/elasticbeanstalk/), an easy-to-use service for deploying and scaling web applications, you may get "413 Request Entity Too Large" errors when posting files larger than 10MB.

You can allow bigger file uploads by creating a file named `src/main/resources/.ebextensions/nginx/conf.d/proxy.conf` with content:

```
client_max_body_size 100M;
```

To learn more, read [Configuring the Reverse Proxy](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/java-se-nginx.html) section in the Elastic Beanstalk documentation.
