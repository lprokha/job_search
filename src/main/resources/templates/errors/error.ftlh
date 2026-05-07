<#import "../layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <link rel="stylesheet" href="/css/error_styles.css">

    <div class="error_block">
        <div class="main_block">
            <div class="content">

                <h1>${status!}</h1>
                <h2>${reason!}</h2>

                <#if details??>
                    <ul class="details">

                        <#if details.serverName??>
                            <li>
                                <span>
                                    <@spring.message "error.host"/>:
                                </span>

                                <code>${details.serverName}</code>
                            </li>
                        </#if>

                        <#if details.requestURL??>
                            <li>
                                <span>
                                    <@spring.message "error.originalUri"/>:
                                </span>

                                <code>${details.requestURL}</code>
                            </li>
                        </#if>

                        <#if details.serverPort??>
                            <li>
                                <span>
                                    <@spring.message "error.servicePort"/>:
                                </span>

                                <code>${details.serverPort}</code>
                            </li>
                        </#if>

                        <#if details.requestId??>
                            <li>
                                <span>
                                    <@spring.message "error.requestId"/>:
                                </span>

                                <code>${details.requestId}</code>
                            </li>
                        </#if>

                        <li>
                            <span>
                                <@spring.message "error.timestamp"/>:
                            </span>

                            <code>${.now}</code>
                        </li>
                    </ul>
                </#if>

                <a href="/vacancies" class="btn btn-dark mt-3">
                    <@spring.message "button.home"/>
                </a>
            </div>
        </div>
    </div>

    <script src="/js/error_js.js"></script>
</@main.layout>