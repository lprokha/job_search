<#import "layouts/layout.ftlh" as main>

<@main.layout>
    <div class="card shadow-sm border-0 mb-4">
        <div class="card-body">
            <h2>${user.name} ${user.surname}</h2>
            <p class="text-muted mb-1">${user.email}</p>
            <p class="mb-1">${user.phoneNumber!""}</p>
            <span class="badge text-bg-secondary">
                <#if user.accountType?string == "APPLICANT">
                    Соискатель
                <#else>
                    Работодатель
                </#if>
            </span>

            <div class="mt-3">
                <p class="mb-2">
                    <strong>Текущее фото:</strong>
                    <#if user.avatar?? && user.avatar?has_content>
                        ${user.avatar}
                    <#else>
                        не установлено
                    </#if>
                </p>

                <form action="/profile/avatar" method="post" enctype="multipart/form-data">
                    <div class="row g-2 align-items-center">
                        <div class="col-md-8">
                            <input type="file" name="file" class="form-control" accept="image/*">
                        </div>
                        <div class="col-md-4">
                            <button type="submit" class="btn btn-outline-dark w-100">Загрузить фото</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div class="mb-4 d-flex gap-2">
        <a href="/profile/edit" class="btn btn-dark btn-sm">Редактировать профиль</a>
    </div>

    <#if user.accountType?string == "APPLICANT">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h3 class="mb-0">Мои резюме</h3>
            <div class="d-flex gap-2">
                <a href="/resumes/create" class="btn btn-dark btn-sm">Добавить</a>
                <a href="/resumes" class="btn btn-outline-dark btn-sm">Смотреть все</a>
            </div>
        </div>

        <#if resumes?? && (resumes?size > 0)>
            <div class="row">
                <#list resumes as resume>
                    <div class="col-md-6 mb-3">
                        <div class="card shadow-sm border-0 h-100">
                            <div class="card-body">
                                <h5>${resume.name}</h5>
                                <p class="mb-2">Зарплата: ${resume.salary!0}</p>
                                <p class="text-muted mb-2">Обновлено: ${resume.updateTime!""}</p>
                                <div class="d-flex gap-2">
                                    <a href="/resumes/edit/${resume.id}" class="btn btn-outline-dark btn-sm">Редактировать</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </#list>
            </div>
        <#else>
            <div class="alert alert-light border">У вас пока нет резюме.</div>
        </#if>
    <#else>
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h3 class="mb-0">Мои вакансии</h3>
            <div class="d-flex gap-2">
                <a href="/my-vacancies/create" class="btn btn-dark btn-sm">Добавить</a>
                <a href="/my-vacancies" class="btn btn-outline-dark btn-sm">Смотреть все</a>
            </div>
        </div>

        <#if vacancies?? && (vacancies?size > 0)>
            <div class="row">
                <#list vacancies as vacancy>
                    <div class="col-md-6 mb-3">
                        <div class="card shadow-sm border-0 h-100">
                            <div class="card-body">
                                <h5>${vacancy.name}</h5>
                                <p class="mb-2">Зарплата: ${vacancy.salary!0}</p>
                                <p class="text-muted mb-2">Обновлено: ${vacancy.updateTime!""}</p>
                                <div class="d-flex gap-2">
                                    <a href="/my-vacancies/edit/${vacancy.id}" class="btn btn-outline-dark btn-sm">Редактировать</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </#list>
            </div>
        <#else>
            <div class="alert alert-light border">У вас пока нет вакансий.</div>
        </#if>
    </#if>
</@main.layout>