<#import "layouts/layout.ftlh" as main>

<@main.layout>
    <h2 class="mb-4">Список вакансий</h2>

    <#if vacancies?? && (vacancies?size > 0)>
        <#list vacancies as vacancy>
            <div class="card shadow-sm border-0 mb-3">
                <div class="card-body">
                    <h4 class="mb-1">${vacancy.name}</h4>
                    <p class="mb-1 fw-semibold">Зарплата: ${vacancy.salary!0}</p>
                    <p class="text-muted mb-2">Обновлено: ${vacancyUpdateTimes[vacancy.id]!""}</p>
                    <p class="mb-0">${vacancy.description!""}</p>
                </div>
            </div>
        </#list>
    <#else>
        <div class="alert alert-light border">Активных вакансий пока нет.</div>
    </#if>
</@main.layout>