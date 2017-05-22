##Build and run

You can use the `service.sh` script to build and run the application.

```
./service.sh           : prints usage
./service.sh dev_build : builds your app
./service.sh dev_run   : runs your app
```


If you have `docker` installed on your machine, you can also run all the
scripts and tests inside a docker container:

```
./service.sh docker_build : packages your app into a docker image
./service.sh docker_run   : runs your app using a docker image
```


## Endpoints


To submit a document for watermarking you can `post` to the watermark endpoint.

```
localhost:8080/api/watermark
```

The body contains the document you want to watermark.

```json
{ "title" : "The Dark Code", "author" : "Bruce Wayne" }
```

This returns a ticket that can used to retrieve the watermarked document.

```json
{
  "id": "Bruce WayneThe Dark Code"
}
```

To retrieve the watermarked document you get send a `get` request to the watermark
endpoint with the ticket id.

```
localhost:8080/api/watermark?ticket=Bruce WayneThe Dark Code
```

This returns a response containing the watermarked document

```json
{
  "title": "The Dark Code",
  "author": "Bruce Wayne",
  "watermark": {
    "title": "The Dark Code",
    "author": "Bruce Wayne",
    "content": "book",
    "topic": "Science"
  },
  "id": "Bruce WayneThe Dark Code"
}
```