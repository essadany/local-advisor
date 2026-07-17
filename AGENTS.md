# LocalAdvisor Engineering Guide

> Load `devops-engineer` for Docker, CI/CD, and deployment.
> Load `vue-patterns` for Vue component and state design.
> Load `java-springboot` for Spring Boot conventions.
> Load `typescript` for type system decisions.

## Philosophy

Write code that is obvious to the next person. Favor explicitness over cleverness.
Every abstraction should earn its keep. Prefer duplication over the wrong abstraction.
If a pattern requires a comment to explain, the pattern is wrong.

---

## Stack Decisions

| Layer | Choice | Rationale |
|-------|--------|-----------|
| Backend | Spring Boot 3, Java 21, Maven | LTS Java, mature ecosystem, GraalVM-ready |
| Frontend | Vue 3, Vite, TypeScript, Pinia | Composition API for scale, Vite for speed, Pinia over Vuex |
| Database | PostgreSQL 16 | JSONB, CTE, robust GIS extensions if needed later |
| Auth | JWT (stateless) | No session store, scales horizontally trivially |
---

## Code Review Standards

Every PR is reviewed against these questions:

1. Does this change have a corresponding test (or justification why not)?
2. Are error states handled, not just the happy path?
3. Could this be simpler? Is the abstraction paying for itself?
4. Are there any security implications (new endpoint, user data exposure)?
5. Are there any performance implications (N+1, missing index, large payloads)?
6. Does the frontend handle loading, empty, error, and edge case states?

If any answer raises a flag, the PR is not merged.

---

## Workflow

```bash
# Local development
docker compose --env-file stack_my_settings up --build

# Run backend tests
cd backend/localadvisor && ./mvnw verify

# Run frontend tests
cd frontend && npm run test:unit

# Build and verify Docker images
docker compose --env-file stack_my_settings build
```

Before pushing, verify: `docker compose --env-file stack_my_settings up --build` succeeds.
After pushing, verify the pipeline builds, tests, and scans without failures.

---

## Checklist

- [ ] Feature has tests (unit + integration where applicable)
- [ ] Docker compose build succeeds
- [ ] No secrets in code
- [ ] No `any` in TypeScript, no `@SuppressWarnings` in Java
- [ ] API changes documented (or issue/PR description updated)
- [ ] Database migration is reversible
- [ ] PR reviewed and green
