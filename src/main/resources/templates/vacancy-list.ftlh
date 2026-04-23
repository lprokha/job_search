<#import "layouts/layout.ftlh" as main>

<@main.layout>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">Список вакансий</h2>

        <form method="get" action="/vacancies" class="d-flex gap-2 align-items-center">
            <input type="hidden" name="page" value="0">

            <label for="sort" class="mb-0">Сортировка:</label>
            <select name="sort" id="sort" class="form-select" onchange="this.form.submit()">
                <option value="dateDesc" <#if sort?? && sort == "dateDesc">selected</#if>>
                    По дате (новые сначала)
                </option>
                <option value="dateAsc" <#if sort?? && sort == "dateAsc">selected</#if>>
                    По дате (старые сначала)
                </option>
                <option value="responsesDesc" <#if sort?? && sort == "responsesDesc">selected</#if>>
                    По откликам (больше сначала)
                </option>
                <option value="responsesAsc" <#if sort?? && sort == "responsesAsc">selected</#if>>
                    По откликам (меньше сначала)
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
                            <a class="page-link" href="/vacancies?page=${pageIndex}&size=5&sort=${sort!'dateDesc'}">
                                ${pageIndex + 1}
                            </a>
                        </li>
                    </#list>
                </ul>
            </nav>
        </#if>
    <#else>
        <div class="alert alert-light border">Активных вакансий пока нет.</div>
    </#if>
</@main.layout>