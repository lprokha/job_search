<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="d-flex flex-column flex-lg-row justify-content-between align-items-start align-items-lg-center gap-3 mb-4">
        <h2 class="mb-0">
            <@spring.message "nav.myVacancies"/>
        </h2>

        <div class="d-flex flex-column flex-md-row gap-2 align-items-start align-items-md-center">
            <form method="get"
                  action="/my-vacancies"
                  class="d-flex flex-wrap gap-2 align-items-center">

                <input type="hidden" name="page" value="0">

                <label for="sort" class="mb-0 text-nowrap">
                    <@spring.message "label.sort"/>
                </label>

                <select name="sort" id="sort" class="form-select form-select-sm w-auto">
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

                <button type="submit" class="btn btn-outline-dark btn-sm text-nowrap">
                    <@spring.message "button.apply"/>
                </button>
            </form>

            <div class="d-flex flex-wrap gap-2">
                <a href="/my-vacancies/create"
                   class="btn btn-dark btn-sm text-nowrap d-inline-flex align-items-center justify-content-center">
                    <@spring.message "button.addVacancy"/>
                </a>

                <a href="/profile"
                   class="btn btn-outline-dark btn-sm text-nowrap d-inline-flex align-items-center justify-content-center">
                    <@spring.message "button.backToProfile"/>
                </a>
            </div>
        </div>
    </div>

    <#if vacancies?? && (vacancies?size > 0)>
        <div class="list-group shadow-sm">
            <#list vacancies as vacancy>
                <div class="list-group-item list-group-item-action position-relative">
                    <div class="d-flex justify-content-between align-items-start gap-3">
                        <div>
                            <h5 class="mb-2">
                                <a href="/vacancies/${vacancy.id}"
                                   class="stretched-link text-decoration-none text-dark">
                                    ${vacancy.name}
                                </a>
                            </h5>

                            <p class="mb-1">
                                <span class="fw-semibold">
                                    <@spring.message "vacancy.salary"/>:
                                </span>
                                ${vacancy.salary!0}
                            </p>

                            <p class="mb-1">
                                <span class="fw-semibold">
                                    <@spring.message "vacancy.experience"/>:
                                </span>
                                ${vacancy.expFrom!0} - ${vacancy.expTo!0}
                                <@spring.message "vacancy.years"/>
                            </p>

                            <p class="text-muted mb-0">
                                <@spring.message "vacancy.updated"/>:
                                ${vacancyUpdateTimes[vacancy.id?string]!""}
                            </p>
                        </div>

                        <div class="text-nowrap position-relative z-2 d-flex flex-wrap gap-2 justify-content-end">
                            <a href="/my-vacancies/${vacancy.id}/responses"
                               class="btn btn-outline-dark btn-sm text-nowrap">
                                <@spring.message "button.responses"/>
                            </a>

                            <a href="/my-vacancies/edit/${vacancy.id}"
                               class="btn btn-outline-dark btn-sm text-nowrap">
                                <@spring.message "button.edit"/>
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
                            <a class="page-link"
                               href="/my-vacancies?page=${pageIndex}&size=5&sort=${sort!'dateDesc'}">
                                ${pageIndex + 1}
                            </a>
                        </li>
                    </#list>
                </ul>
            </nav>
        </#if>
    <#else>
        <div class="alert alert-light border">
            <@spring.message "vacancies.notFound"/>
        </div>
    </#if>
</@main.layout>