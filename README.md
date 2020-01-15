# nextcloud-setting

A Java commandline tool to get/set https://nextcloud.com/ setting values via REST

Uses https://github.com/col-panic/nextcloud-java-api, 
an adapted version of https://github.com/a-schild/nextcloud-java-api.


## Building

1. Import as Eclipse project
2. Select `src/nextcloud_setting/Main.java`  and `Export` / `Runnable Jar File`

## Usage

```
$> java -jar NextcloudSetting.jar 
The following options are required: [-u], [-l], [-p]

Usage: <main class> [options]
  Options:
  * -l
      username
  * -p
      password
    -s
      values to set as key=value; multiple occurences allowed
      Default: []
    -t
      Trust all HTTPS certificates
      Default: false
  * -u
      Nextcloud service url, e.g. https://nextcloud.instance.com:8443/cloud
    -v
      Verbose output
      Default: false
```

## Examples

```
java -jar NextcloudSetting.jar -t -u https://localhost/cloud -v -l NextcloudAdmin -p password -s theming/name=bla
```

## Changes

* adf
