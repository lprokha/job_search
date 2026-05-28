<#import "../layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-lg-7">
            <div class="card shadow-sm border-0">
                <div class="card-body p-4 text-center">

                    <h2 class="mb-3">
                        <#if status?? && status == 403>
                            <@spring.message "error.forbiddenTitle"/>
                        <#else>
                            <@spring.message "error.title"/>
                        </#if>
                    </h2>

                    <p class="text-muted mb-4">
                        <#if status?? && status == 403>
                            <@spring.message "error.forbiddenMessage"/>
                        <#elseif message??>
                            ${message}
                        <#else>
                            <@spring.message "error.message"/>
                        </#if>
                    </p>

                    <a href="/vacancies" class="btn btn-dark">
                        <@spring.message "nav.vacancies"/>
                    </a>

                    <#if currentUser??>
                        <a href="/profile" class="btn btn-outline-dark ms-2">
                            <@spring.message "nav.profile"/>
                        </a>
                    </#if>

                </div>
            </div>
        </div>
    </div>
</@main.layout>