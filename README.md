* smartly expand to >=6 length of string; already can
* how to avoid someone enumerate
* what if we passed 64bit? -- auto create a new "id", then add; but let's not worry for now
* calculate redis size

numbers:
* long gives you 2**64 urls, max slug length is 10

TODO:
* use gatling to loadtest
* set up nginx -> 2 servers -> redis cluster on aws; setup redis persistence
* write doc
* separate by readonly and writeonly

---
### Capacity
- how many urls can store? How many redis nodes we need for max capacity?
- req/s on servers

### System design
- redis cluster: sharding? persistence; replication

### Further improvements
- spam filter

