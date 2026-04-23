<#import "layouts/layout.ftlh" as main>

<@main.layout>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">Вакансии компании: ${company.name} ${company.surname}</h2>

        <form method="get" action="/companies/${company.id}" class="d-flex gap-2 align-items-center">
            <input type="hidden" name="page" value="0">

            <label for="sort" class="mb-0">Сортировка:</label>
            <select name="sort" id="sort" class="form-select" onchange="this.form.submit()">
                <option value="dateDesc" <#if sort?? && sort == "dateDesc">selected</#if>>
                    По дате (сначала новые)
                </option>
                <option value="dateAsc" <#if sort?? && sort == "dateAsc">selected</#if>>
                    По дате (сначала старые)
                </option>
                <option value="responsesDesc" <#if sort?? && sort == "responsesDesc">selected</#if>>
                    По откликам (сначала больше)
                </option>
                <option value="responsesAsc" <#if sort?? && sort == "responsesAsc">selected</#if>>
                    По откликам (сначала меньше)
                </option>
            </select>
        </form>
    </div>

    <#if vacancies?? && (vacancies?size > 0)>
        <#list vacancies as vacancy>
            <div class="card shadow-sm border-0 mb-3">
                <div class="card-body">
                    <h4 class="mb-1">${vacancy.name}</h4>
                    <p class="mb-1 fw-semibold">Зарплата: ${vacancy.salary!0}</p>
                    <p class="text-muted mb-2">Обновлено: ${vacancyUpdateTimes[vacancy.id?string]!""}</p>
                    <p class="mb-0">${vacancy.description!""}</p>
                </div>
            </div>
        </#list>

        <#if totalPages?? && (totalPages > 1)>
            <nav class="mt-4">
                <ul class="pagination">
                    <#list 0..(totalPages - 1) as pageIndex>
                        <li class="page-item <#if pageIndex == currentPage>active</#if>">
                            <a class="page-link"
                               href="/companies/${company.id}?page=${pageIndex}&size=5&sort=${sort!'dateDesc'}">
                                ${pageIndex + 1}
                            </a>
                        </li>
                    </#list>
                </ul>
            </nav>
        </#if>
    <#else>
        <div class="alert alert-light border">У этой компании пока нет вакансий.</div>
    </#if>
</@main.layout>