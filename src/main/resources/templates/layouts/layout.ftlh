<#macro layout>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>JobSearch</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light d-flex flex-column min-vh-100">

    <nav class="navbar navbar-expand-lg bg-white border-bottom shadow-sm">
        <div class="container">
            <a class="navbar-brand fw-bold" href="/vacancies">JobSearch</a>

            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="mainNavbar">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="/vacancies">Вакансии</a>
                    </li>

                    <#if currentUser??>
                        <li class="nav-item">
                            <a class="nav-link" href="/profile">Личный кабинет</a>
                        </li>

                        <#if currentUser.accountType?string == "APPLICANT">
                            <li class="nav-item">
                                <a class="nav-link" href="/resumes">Мои резюме</a>
                            </li>
                        </#if>

                        <#if currentUser.accountType?string == "EMPLOYER">
                            <li class="nav-item">
                                <a class="nav-link" href="/my-vacancies">Мои вакансии</a>
                            </li>
                        </#if>
                    </#if>
                </ul>

                <div class="d-flex gap-2">
                    <#if !user??>
                        <a href="/login" class="btn btn-outline-dark btn-sm">Войти</a>
                        <a href="/register" class="btn btn-dark btn-sm">Регистрация</a>
                    </#if>

                    <#if user??>
                        <form action="/logout" method="post" class="d-inline">
                            <button type="submit" class="btn btn-outline-secondary btn-sm">Выйти</button>
                        </form>
                    </#if>
                </div>
            </div>
        </div>
    </nav>

    <main class="container py-4 flex-grow-1">
        <#nested>
    </main>

    <footer class="bg-white border-top mt-auto">
        <div class="container py-3 text-center text-muted small">
            JobSearch © 2026
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
    </html>
</#macro>