# CLAUDE.md

## Commit Strategy

### Commit Message Format

`<issue-key> <commit-message-summary>`

- 브랜치명에서 이슈 키를 자동 추출한다.
  - 예: `feature/PSN-49-modal` -> `PSN-49`
- 별도의 husky 스크립트나 정규표현식 없이 브랜치명 컨벤션으로 처리한다.

### Commit Message Template

```
<issue-key> 간단한 설명

## 변경사항 (사용자: X% | Claude: Y%)
### 사용자 작성 (X%):
- [사용자가 작성한 변경사항들]

### Claude 작성 (Y%):
- [Claude가 작성한 변경사항들 (파일 경로 포함)]

Co-Authored-By: Claude <noreply@anthropic.com>
```

### Rules

- 커밋 본문에는 사용자가 작성한 부분과 AI가 작성한 부분을 구분하여 기록한다.
- Claude가 작성한 변경사항에는 파일 경로를 반드시 포함한다.
- 기여도 비율(X%, Y%)은 실제 코드 변경량을 기준으로 산정한다.
- 이슈 키는 현재 브랜치명에서 추출하며, 브랜치명에 이슈 키가 없으면 생략한다.
