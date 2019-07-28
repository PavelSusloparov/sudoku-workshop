# Calculator

## Generate application image

```bash
./gradlew jibDockerBuild
```

## Solve sudoku
```bash
SUDOKU="\
|5.6|8..|2..\
|...|45.|.8.\
|.43|..1|...\
|---+---+---\
|.78|5..|6.2\
|...|.7.|...\
|6.1|..8|37.\
|---+---+---\
|...|3..|72.\
|.2.|.64|...\
|..5|..2|4.1\
"
```
```bash
curl -H "Content-Type: application/json" -d "{\"sudoku\": \"$SUDOKU\"}" -X POST http://localhost:8102/sudoku/v1/solve | jq '.result' | awk '{gsub(/\\n/,"\n")}1'
```
