# Gauge Requirements Report Plugin

A report plugin for https://gauge.org.

## Prerequisites

- Gauge
- JDK 1.8.

## Building

- clone/fork this repo
- Ensure that your
- `./gradlew build` - creates the binaries (using gradle's default build, no magic here)
- `./gradlew distro` - creates a zipped distributable gauge plugin
- `gauge install gauge-requirements-report -f artifacts/gauge-requirements-report-0.0.1.zip` - use gauge's file install to install the plugin.

## Usage

- `gauge init <language you like>`
- Edit `manifest.json`, add `gauge-requirements-report` to `plugins`.
- Add for each scenario in your project a tag with the requirement id(s), which the scenario is testing, like
```
# A scenario
tags: SONAR-1564, SONAR-1054, fast
* Step 1
* Step 2

# Another scenario
tags: SONAR-1054
* Step 1
* Step 2
```

- Create a `requirements.properties` file in the `env/default` folder (or respectively in the environment folder you use) with the following content
```
# the base link to your requirement system
# if empty or missing, no link to the requirement system will be in the report
gauge.requirements.link = https://jira.sonarsource.com/browse/

# the pattern to match your format of the requirement (this e.g. would match SONAR-1054)
# if empty or missing, no tags will be identified and the report will be empty
gauge.requirements.pattern = \\w+-\\d+

# all requirements that must be tested in the given scenarios
# if empty or missing, no coverage information will be in the report
gauge.requirements.ids = SONAR-10562, SONAR-10561, SONAR-10565
```

## Report
The report is in JSON format and shows for each identified tag a list of scenarios with their execution status.

In addition, when the `gauge.requirements.ids` property is present, it contains all ids, that are part of thie list,
for which no scenarios have been found. This means, that there are requirements without a test case existing.

An example report

```
{
  "requirements": [
    {
      "id": "SONAR-10562",
      "link": "https://jira.sonarsource.com/browse/SONAR-10562",
      "testResults": [
        {
          "id": "",
          "heading": "Vowel counts in single word",
          "executionTime": 3,
          "status": "PASSED"
        }
      ],
      "total": 1,
      "passed": 1
    },
    {
      "id": "SONAR-10561",
      "link": "https://jira.sonarsource.com/browse/SONAR-10561",
      "testResults": [
        {
          "id": "",
          "heading": "Vowel counts in single word",
          "executionTime": 3,
          "status": "PASSED"
        },
        {
          "id": "",
          "heading": "Vowel counts in multiple word",
          "executionTime": 0,
          "status": "PASSED"
        }
      ],
      "total": 2,
      "passed": 2
    }
  ],
  "missingIds": [
    "SONAR-10565"
  ]
}
```

# License

MIT.