<#import "layouts/layout.ftlh" as main>

<@main.layout>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">Компании</h2>
    </div>

    <#if companies?? && (companies?size > 0)>
        <div class="row">
            <#list companies as company>
                <div class="col-md-6 col-lg-4 mb-3">
                    <div class="card shadow-sm border-0 h-100">
                        <div class="card-body d-flex flex-column">
                            <h5 class="mb-2">${company.name} ${company.surname}</h5>
                            <p class="text-muted mb-3">
                                Вакансий: ${companyVacancyCounts[company.id?string]!0}
                            </p>

                            <div class="mt-auto">
                                <a href="/companies/${company.id}" class="btn btn-outline-dark btn-sm">
                                    Смотреть вакансии
                                </a>
                            </div>
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
        <div class="alert alert-light border">Компании не найдены.</div>
    </#if>
</@main.layout>