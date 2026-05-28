<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">
            <@spring.message "companyVacancies.title"/>:
            ${company.name} ${company.surname}
        </h2>

        <form method="get" action="/companies/${company.id}" class="d-flex gap-2 align-items-center">
            <input type="hidden" name="page" value="0">

            <label for="sort" class="mb-0">
                <@spring.message "label.sort"/>
            </label>

            <select name="sort" id="sort" class="form-select">
                <option value="dateDesc" <#if sort?? && sort == "dateDesc">selected</#if>>
                    <@spring.message "sort.dateDesc"/>
                </option>
                <option value="dateAsc" <#if sort?? && sort == "dateAsc">selected</#if>>
                    <@spring.message "sort.dateAsc"/>
                </option>
                <option value="responsesDesc" <#if sort?? && sort == "responsesDesc">selected</#if>>
                    <@spring.message "sort.responsesDesc"/>
                </option>
                <option value="responsesAsc" <#if sort?? && sort == "responsesAsc">selected</#if>>
                    <@spring.message "sort.responsesAsc"/>
                </option>
            </select>

            <button type="submit" class="btn btn-outline-dark">
                <@spring.message "button.apply"/>
            </button>
        </form>
    </div>

    <#if vacancies?? && (vacancies?size > 0)>
        <#list vacancies as vacancy>
            <div class="card shadow-sm border-0 mb-3">
                <div class="card-body">
                    <h5 class="mb-2">
                        <a href="/vacancies/${vacancy.id}" class="text-decoration-none text-dark">
                            ${vacancy.name}
                        </a>
                    </h5>

                    <p class="mb-1 fw-semibold">
                        <@spring.message "vacancy.salary"/>:
                        ${vacancy.salary!0}
                    </p>

                    <p class="text-muted mb-2">
                        <@spring.message "vacancy.updated"/>:
                        ${vacancyUpdateTimes[vacancy.id?string]!""}
                    </p>

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
        <div class="alert alert-light border">
            <@spring.message "companyVacancies.empty"/>
        </div>
    </#if>
</@main.layout>