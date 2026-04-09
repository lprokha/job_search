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
        </div>
    </div>

    <div class="mb-4">
        <a href="/profile/edit" class="btn btn-dark btn-sm">Редактировать профиль</a>
    </div>

    <#if user.accountType?string == "APPLICANT">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h3 class="mb-0">Мои резюме</h3>
            <a href="/resumes" class="btn btn-outline-dark btn-sm">Смотреть все</a>
        </div>

        <#if resumes?? && (resumes?size > 0)>
            <div class="row">
                <#list resumes as resume>
                    <div class="col-md-6 mb-3">
                        <div class="card shadow-sm border-0 h-100">
                            <div class="card-body">
                                <h5>${resume.name}</h5>
                                <p class="mb-2 text-muted">Обновлено: ${resume.updateTime!""}</p>
                                <div class="d-flex gap-2">
                                    <button class="btn btn-outline-dark btn-sm" type="button">Редактировать</button>
                                    <button class="btn btn-dark btn-sm" type="button">Обновить</button>
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
            <a href="/my-vacancies" class="btn btn-outline-dark btn-sm">Смотреть все</a>
        </div>

        <#if vacancies?? && (vacancies?size > 0)>
            <div class="row">
                <#list vacancies as vacancy>
                    <div class="col-md-6 mb-3">
                        <div class="card shadow-sm border-0 h-100">
                            <div class="card-body">
                                <h5>${vacancy.name}</h5>
                                <p class="mb-2 text-muted">Обновлено: ${vacancy.updateTime!""}</p>
                                <div class="d-flex gap-2">
                                    <button class="btn btn-outline-dark btn-sm" type="button">Редактировать</button>
                                    <button class="btn btn-dark btn-sm" type="button">Обновить</button>
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