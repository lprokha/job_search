<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">
            <@spring.message "vacancies.title"/>
        </h2>

        <form method="get" action="/vacancies" class="d-flex gap-2 align-items-center">
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

    <div class="card shadow-sm border-0 mb-4">
        <div class="card-body">
            <h5 class="mb-3">
                <@spring.message "vacancy.filterTitle"/>
            </h5>

            <form id="vacancy-filter-form" class="row g-3">
                <div class="col-md-4">
                    <label for="filterText" class="form-label">
                        <@spring.message "vacancy.filterText"/>
                    </label>

                    <input type="text"
                           id="filterText"
                           name="filterText"
                           class="form-control"
                           placeholder="<@spring.message 'vacancy.filterTextPlaceholder'/>">
                </div>

                <div class="col-md-4">
                    <label for="filterCategory" class="form-label">
                        <@spring.message "label.category"/>
                    </label>

                    <select id="filterCategory"
                            name="filterCategory"
                            class="form-select">
                        <option value="">
                            <@spring.message "vacancy.anyCategory"/>
                        </option>

                        <#if categories??>
                            <#list categories as category>
                                <option value="${category.id}">
                                    ${category.name}
                                </option>
                            </#list>
                        </#if>
                    </select>
                </div>

                <div class="col-md-4">
                    <label for="filterExperience" class="form-label">
                        <@spring.message "vacancy.filterExperience"/>
                    </label>

                    <input type="number"
                           id="filterExperience"
                           name="filterExperience"
                           class="form-control"
                           min="0"
                           placeholder="<@spring.message 'vacancy.filterExperiencePlaceholder'/>">
                </div>

                <div class="col-md-4">
                    <label for="filterSalaryFrom" class="form-label">
                        <@spring.message "vacancy.salaryFrom"/>
                    </label>

                    <input type="number"
                           id="filterSalaryFrom"
                           name="filterSalaryFrom"
                           class="form-control"
                           min="0">
                </div>

                <div class="col-md-4">
                    <label for="filterSalaryTo" class="form-label">
                        <@spring.message "vacancy.salaryTo"/>
                    </label>

                    <input type="number"
                           id="filterSalaryTo"
                           name="filterSalaryTo"
                           class="form-control"
                           min="0">
                </div>

                <div class="col-md-4 d-flex align-items-end gap-2">
                    <button type="button"
                            id="applyVacancyFilter"
                            class="btn btn-dark">
                        <@spring.message "button.applyFilter"/>
                    </button>

                    <button type="button"
                            id="clearVacancyFilter"
                            class="btn btn-outline-dark">
                        <@spring.message "button.clear"/>
                    </button>
                </div>
            </form>
        </div>
    </div>

    <div id="vacancy-filter-empty" class="alert alert-light border d-none">
        <@spring.message "vacancies.filteredEmpty"/>
    </div>

    <#if vacancies?? && (vacancies?size > 0)>
        <div id="vacancy-list">
            <#list vacancies as vacancy>
                <div class="card shadow-sm border-0 mb-3 vacancy-item"
                     data-name="${(vacancy.name!'')?lower_case}"
                     data-description="${(vacancy.description!'')?lower_case}"
                     data-category-id="${(vacancy.category.id)!''}"
                     data-salary="<#if vacancy.salary??>${vacancy.salary?c}<#else>0</#if>"
                     data-exp-from="<#if vacancy.expFrom??>${vacancy.expFrom?c}<#else>0</#if>"
                     data-exp-to="<#if vacancy.expTo??>${vacancy.expTo?c}<#else>0</#if>">

                    <div class="card-body">
                        <h4 class="mb-1">
                            <a href="/vacancies/${vacancy.id}" class="text-decoration-none text-dark">
                                ${vacancy.name}
                            </a>
                        </h4>

                        <p class="mb-1 fw-semibold">
                            <@spring.message "vacancy.salary"/>: ${vacancy.salary!0}
                        </p>

                        <p class="mb-1">
                            <@spring.message "vacancy.experience"/>:
                            ${vacancy.expFrom!0} - ${vacancy.expTo!0}
                            <@spring.message "vacancy.years"/>
                        </p>

                        <p class="text-muted mb-2">
                            <@spring.message "vacancy.updated"/>:
                            ${vacancyUpdateTimes[vacancy.id?string]!""}
                        </p>

                        <p class="mb-0">${vacancy.description!""}</p>
                    </div>
                </div>
            </#list>
        </div>

        <nav class="mt-4">
            <ul id="vacancy-pagination" class="pagination"></ul>
        </nav>
    <#else>
        <div class="alert alert-light border">
            <@spring.message "vacancies.empty"/>
        </div>
    </#if>

    <script src="/static/js/vacancy-filter.js"></script>
</@main.layout>