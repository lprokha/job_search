<#import "layouts/layout.ftlh" as main>

<@main.layout>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Мои вакансии</h2>
        <a href="/profile" class="btn btn-outline-dark">Назад в личный кабинет</a>
    </div>

    <#if vacancies?? && (vacancies?size > 0)>
        <div class="row">
            <#list vacancies as vacancy>
                <div class="col-md-6 col-lg-4 mb-3">
                    <div class="card shadow-sm border-0 h-100">
                        <div class="card-body">
                            <h5>${vacancy.name}</h5>
                            <p class="mb-2">Зарплата: ${vacancy.salary!0}</p>
                            <p class="mb-2">Опыт: ${vacancy.expFrom!0} - ${vacancy.expTo!0} лет</p>
                            <p class="text-muted mb-2">Обновлено: ${vacancy.updateTime!""}</p>
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
        <div class="alert alert-light border">Вакансии не найдены.</div>
    </#if>
</@main.layout>