<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">
            <@spring.message "companies.title"/>
        </h2>
    </div>

    <#if companies?? && (companies?size > 0)>
        <div class="list-group shadow-sm">
            <#list companies as company>
                <div class="list-group-item list-group-item-action">
                    <div class="d-flex justify-content-between align-items-start gap-3">
                        <div>
                            <h5 class="mb-2">${company.name} ${company.surname}</h5>

                            <p class="text-muted mb-0">
                                <@spring.message "companies.vacancyCount"/>:
                                ${companyVacancyCounts[company.id?string]!0}
                            </p>
                        </div>

                        <div class="text-nowrap">
                            <a href="/companies/${company.id}" class="btn btn-outline-dark btn-sm">
                                <@spring.message "button.viewVacancies"/>
                            </a>
                        </div>
                    </div>
                </div>
            </#list>
        </div>

        <#if totalPages?? && (totalPages > 1)>
            <nav class="mt-4">
                <ul class="pagination">
                    <#list 0..(totalPages - 1) as pageIndex>
                        <li class="page-item <#if pageIndex == currentPage>active</#if>">
                            <a class="page-link" href="/companies?page=${pageIndex}&size=6">
                                ${pageIndex + 1}
                            </a>
                        </li>
                    </#list>
                </ul>
            </nav>
        </#if>
    <#else>
        <div class="alert alert-light border">
            <@spring.message "companies.notFound"/>
        </div>
    </#if>
</@main.layout>