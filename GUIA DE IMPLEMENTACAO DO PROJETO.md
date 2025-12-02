# Guia de Implementação do Projeto

Este guia consolida todos os endpoints REST do back-end RunUnit, mapeando caminhos, métodos, payloads de requisição e formatos de resposta. Utilize-o como referência rápida para implementar clientes ou testar a API.

## Convenções Gerais
- Todas as rotas são prefixadas por `/api` quando indicado no controlador.
- Campos marcados como **opcional** podem ser omitidos ou enviados como `null`.
- Datas utilizam `ISO-8601` (por exemplo, `2024-05-20T10:00:00Z`).
- Autenticação/ autorização seguem as regras indicadas no controlador (ex.: `@PreAuthorize`).

## Autenticação
### Login
- **POST** `/api/auth/login`
- **Body**
```json
{
  "email": "usuario@example.com",
  "password": "senhaSegura"
}
```
- **Resposta 200**
```json
{
  "token": "jwt...",
  "id": 1,
  "name": "Nome",
  "lastName": "Sobrenome",
  "email": "usuario@example.com",
  "role": "USER"
}
```

### Registro
- **POST** `/api/auth/register`
- **Body**
```json
{
  "name": "Nome",
  "lastName": "Sobrenome",
  "birthDate": "1990-01-01",
  "gender": "MALE",
  "timezone": "America/Sao_Paulo",
  "locale": "pt-BR",
  "email": "usuario@example.com",
  "password": "senhaSegura"
}
```
- **Resposta 200**: `UserResponseDto` com metadados completos do usuário.

### Login com Google
- **GET** `/api/auth/google` → retorna URL de autenticação.
- **GET** `/api/auth/google/callback?code=...` → responde `LoginResponse` (mesma estrutura do login padrão).

## Usuários
### Criar usuário (admin)
- **POST** `/api/users`
- **Body**: igual ao registro.
- **Resposta 201**: `UserResponseDto` com `Location` para o novo recurso.

### Listar usuários
- **GET** `/api/users/`
- **Resposta 200**: lista de `UserResponseDto`.

### Buscar usuário por ID
- **GET** `/api/users/{id}`
- **Resposta 200**: `UserResponseDto` ou `404` se não encontrado.

### Atualizar perfil (autenticado)
- **PUT** `/api/users/profile`
- **Body**
```json
{
  "name": "Novo",
  "lastName": "Nome",
  "birthDate": "1991-02-02",
  "gender": "FEMALE",
  "timezone": "UTC",
  "locale": "en-US",
  "profilePictureUrl": "https://..."
}
```
- **Resposta 200**: `UserResponseDto` atualizado.

### Atualizar senha (autenticado)
- **PUT** `/api/users/password`
- **Body**
```json
{
  "currentPassword": "senhaAtual",
  "newPassword": "novaSenha",
  "newPasswordConfirmation": "novaSenha"
}
```
- **Resposta 204** sem corpo.

### Excluir usuário
- **DELETE** `/api/users/{id}` → resposta `204`.

## Tipos de Associação
### Criar tipo
- **POST** `/api/membership-types`
- **Body**
```json
{
  "name": "Premium",
  "monthlyPrice": 49.90,
  "description": "Acesso completo"
}
```
- **Resposta 201**: `MembershipTypeResponseDto` com `Location` do recurso.

### Listar tipos
- **GET** `/api/membership-types`
- **Resposta 200**: lista de `MembershipTypeResponseDto`.

### Detalhar tipo
- **GET** `/api/membership-types/{id}` → `MembershipTypeResponseDto`.

### Atualizar tipo
- **PUT** `/api/membership-types/{id}`
- **Body**: mesmo formato da criação, campos opcionais.
- **Resposta 200**: `MembershipTypeResponseDto` atualizado.

### Excluir tipo
- **DELETE** `/api/membership-types/{id}` → `204`.

## Corridas
### Criar corrida
- **POST** `/api/races`
- **Body** (principais campos)
```json
{
  "name": "Corrida da Cidade",
  "raceDate": "2024-08-01T09:00:00Z",
  "venueName": "Parque Central",
  "registrationUrl": "https://inscricao.com",
  "organizerContact": "organizador@exemplo.com",
  "city": "São Paulo",
  "state": "SP",
  "latitude": -23.5505,
  "longitude": -46.6333,
  "maxParticipants": 5000,
  "raceDistanceKm": 10,
  "status": "SCHEDULED"
}
```
- **Resposta 201**: `RaceResponseDto` com `id` e `createdAt`.

### Listar corridas
- **GET** `/api/races` → lista de `RaceResponseDto`.

### Detalhar corrida
- **GET** `/api/races/{id}` → `RaceResponseDto`.

### Atualizar corrida
- **PUT** `/api/races/{id}`
- **Body**: mesmos campos da criação (todos opcionais) + `status`.
- **Resposta 200**: `RaceResponseDto` atualizado.

### Excluir corrida
- **DELETE** `/api/races/{id}` → `204`.

### Buscar por filtros
- **GET** `/api/races/search?name=&location=&startDate=&endDate=`
- **Resposta 200**: lista de corridas que atendem aos filtros.

## Inscrições do Usuário em Corridas
### Registrar-se em corrida
- **POST** `/api/user-races`
- **Body**
```json
{
  "raceId": 10,
  "tag": "PARTICIPANT"
}
```
- **Resposta 201**: `UserRaceResponseDto` com `userId`, `raceId` e `active`.

### Minhas corridas
- **GET** `/api/user-races/my` → lista de `UserRaceResponseDto` do usuário autenticado.

### Usuários de uma corrida
- **GET** `/api/user-races/race/{raceId}` → lista de inscritos.

### Detalhar minha inscrição
- **GET** `/api/user-races/{raceId}` → `UserRaceResponseDto` do usuário autenticado.

### Atualizar minha inscrição
- **PUT** `/api/user-races/{raceId}`
- **Body**
```json
{
  "tag": "PACER",
  "active": true
}
```
- **Resposta 200**: `UserRaceResponseDto` atualizado.

### Cancelar inscrição
- **DELETE** `/api/user-races/{raceId}` → `204`.

## Sessões de Corrida
### Registrar sessão
- **POST** `/api/sessions`
- **Body**
```json
{
  "raceId": 10,
  "startedAt": "2024-05-20T10:00:00Z",
  "elapsedSeconds": 3600,
  "distanceMeters": 10000,
  "paceMinPerKm": 6.0,
  "avgHeartRate": 150,
  "peakHeartRate": 175,
  "elevationGainMeters": 120.5
}
```
- **Resposta 201**: `RunningSessionResponseDto` com `id`, `userId` e `active`.

### Listar minhas sessões
- **GET** `/api/sessions` → lista de `RunningSessionResponseDto` do usuário.

### Detalhar sessão
- **GET** `/api/sessions/{id}` → `RunningSessionResponseDto` (autorização conforme regra de proprietário ou papel).

### Atualizar sessão
- **PUT** `/api/sessions/{id}`
- **Body**: mesmos campos da criação (opcionais) + `active`.
- **Resposta 200**: `RunningSessionResponseDto` atualizado.

### Excluir sessão
- **DELETE** `/api/sessions/{id}` → `204`.

## Biometria do Usuário
### Registrar biometria
- **POST** `/api/my/biometrics`
- **Body**
```json
{
  "facialEmbeddingBase64": "base64..."
}
```
- **Resposta 201**: `UserBiometricsResponseDto` com `biometricActive` e `registeredAt`.

### Ver status
- **GET** `/api/my/biometrics` → `UserBiometricsResponseDto` ou `404` se inexistente.

### Atualizar biometria
- **PUT** `/api/my/biometrics`
- **Body**
```json
{
  "facialEmbeddingBase64": "novoBase64...",
  "biometricActive": true
}
```
- **Resposta 200**: `UserBiometricsResponseDto` atualizado.

### Excluir biometria
- **DELETE** `/api/my/biometrics` → `204`.

## Conquistas
### Criar conquista (admin)
- **POST** `/api/admin/achievements`
- **Body**
```json
{
  "name": "10K Completo",
  "description": "Primeira prova de 10 km",
  "iconUrl": "https://.../10k.png",
  "criteria": [
    {
      "metricType": "TOTAL_DISTANCE",
      "requiredValue": 10.0,
      "comparisonOperator": "GREATER_OR_EQUAL"
    }
  ]
}
```
- **Resposta 201**: `AchievementResponseDto` com lista de critérios.

### Listar conquistas
- **GET** `/api/admin/achievements` → lista de `AchievementResponseDto`.

### Detalhar conquista
- **GET** `/api/admin/achievements/{id}` → `AchievementResponseDto`.

### Atualizar conquista
- **PUT** `/api/admin/achievements/{id}`
- **Body**: mesmos campos da criação (opcionais) + `active` e critérios completos.
- **Resposta 200**: `AchievementResponseDto` atualizado.

### Excluir conquista
- **DELETE** `/api/admin/achievements/{id}` → `204`.

## Conquistas do Usuário
### Listar minhas conquistas
- **GET** `/api/my/achievements` → lista de `UserAchievementResponseDto` (campos: `achievementId`, `achievementName`, `achievedAt`, `achievedValue`).

### Detalhar conquista específica
- **GET** `/api/my/achievements/{achievementId}` → `UserAchievementResponseDto` do usuário.

### Revogar conquista de usuário (admin)
- **DELETE** `/api/admin/user-achievements/{userId}/{achievementId}` → `204`.

## Blog
### Categorias (admin)
- **POST** `/api/admin/blog-categories` → cria categoria (`name`, `slug`, `description`).
- **GET** `/api/admin/blog-categories` → lista `BlogCategoryResponseDto`.
- **GET** `/api/admin/blog-categories/{id}` → detalha categoria.
- **PUT** `/api/admin/blog-categories/{id}` → atualiza (`name`, `description`, `active`).
- **DELETE** `/api/admin/blog-categories/{id}` → `204`.

### Posts (admin)
- **POST** `/api/admin/blog-posts`
- **Body**
```json
{
  "title": "Título",
  "slug": "titulo",
  "content": "Corpo do post",
  "excerpt": "Resumo opcional",
  "thumbnailUrl": "https://.../thumb.jpg",
  "authorId": 1,
  "categoryIds": [1, 2]
}
```
- **Resposta 201**: `BlogPostResponseDto`.
- **GET** `/api/admin/blog-posts` → lista `BlogPostResponseDto` (qualquer status).
- **GET** `/api/admin/blog-posts/{id}` → detalhe.
- **PUT** `/api/admin/blog-posts/{id}` → atualiza campos (`title`, `content`, `excerpt`, `thumbnailUrl`, `status`, `publicationDate`, `categoryIds`).
- **DELETE** `/api/admin/blog-posts/{id}` → `204`.

### Posts (público)
- **GET** `/api/blog/posts` → posts publicados (`BlogPostResponseDto`).
- **GET** `/api/blog/posts/{id}` → detalhe público.

## Observações Finais
- Payloads seguem validações declaradas nos DTOs (ex.: tamanhos máximos, obrigatoriedade, valores mínimos).
- Endpoints que criam recursos retornam `Location` para o novo registro quando aplicável.
- Ajuste cabeçalhos de autenticação conforme mecanismo JWT (`Authorization: Bearer <token>`), onde a segurança estiver habilitada.
