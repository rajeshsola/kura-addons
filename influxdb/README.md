## InfluxDb Service Bundle

Supported methods via InfluxDbClient interface, use `getInfluxDbClient` to obtain InfluxDbClient instance

```
ping

getVersion

setLogLevel

setConsistencyLevel

enableBatch

disableBatch

setDatabaseName

setRetentionPolicy

createDatabase

dropDatabase

writeLine(measurement, fieldset, tagset)

writeLine(measurement, fieldset)

prepareLineProtocol

addFieldEntry

addTagEntry

writeLine
```
## InfluxDb Demo Bundle

Create a client instance using getInfluxDbClient(), suitably in activate

Use the supported methods according to data flow

Disable batch mode in deactivate if activated earlier

## Contributors

Rajesh Sola

Vibhor Khurana


