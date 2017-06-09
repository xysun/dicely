* smartly expand to >=6 length of string; already can
* how to avoid someone enumerate
* what if we passed 64bit? -- auto create a new "id", then add; but let's not worry for now
* calculate redis size

numbers:
* long gives you 2**64 urls, max slug length is 10

test case:
* {"url":"github.com/lemonlabsuk/scala-uri"} should pass
* {"url":"github.com/lemonlabsuk/scala-uri"} should give same result as {"url":"http://github.com/lemonlabsuk/scala-uri"}