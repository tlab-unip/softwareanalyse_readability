## Build Project

```ps1
mvn install
```

```ps1
mvn test -Dtest=FeaturesTest -q
```

## Running CLI

### Preprocess

```ps1
java -jar $pwd/target/Readability-Analysis-1.0.jar preprocess -g $pwd/resources/truth_scores.csv -s $pwd/resources/snippets -t $pwd/resources/target.csv LINES TOKEN_ENTROPY H_VOLUME
```

### Classify

```ps1
java -jar $pwd/target/Readability-Analysis-1.0.jar classify -d $pwd/resources/target.csv
```