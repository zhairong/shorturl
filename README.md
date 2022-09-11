design thinking
###
* this application will generate a shorter and unique alias for a given url (for url provider).
* user(end user) access our application with shorturl will be redirected to the original url.
* the mapping shorturl to orignal url should be invalidated after a timespan if the shorturl not used.
* URL redirection should happen in real-time with minimal latency.
* shorturl should be random generated, not guessable.
* to find out:  memories, storage for how many usages.
### API
* reading map shorturl to longurl
* writing add mapping (shorturl=>longurl)

### Impl
* strategy for generating shorturl with longurl.

### Usage
* forward request with shorturl to longurl. (http 30x)

### to considering
* how many entries should be considered.
* limit of amount generated shorturl, and solution.

### Version 1
* save map ins cache, no database.
* resolve longurl from cache, no database.
* build shorturl generator, build shorturl redirect
* build read API, resolve API.

### Prototyp 
* add map short to long
* read shorturl and return longurl
* redirect shorturl to longurl if exists otherwise to default url.

# 