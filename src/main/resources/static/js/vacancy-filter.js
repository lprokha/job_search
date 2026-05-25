window.addEventListener('load', function () {
    const filterKey = 'vacancyFilter';
    const pageSize = 5;

    const filterForm = document.getElementById('vacancy-filter-form');
    const filterText = document.getElementById('filterText');
    const filterCategory = document.getElementById('filterCategory');
    const filterExperience = document.getElementById('filterExperience');
    const filterSalaryFrom = document.getElementById('filterSalaryFrom');
    const filterSalaryTo = document.getElementById('filterSalaryTo');
    const applyButton = document.getElementById('applyVacancyFilter');
    const clearButton = document.getElementById('clearVacancyFilter');
    const emptyBlock = document.getElementById('vacancy-filter-empty');
    const pagination = document.getElementById('vacancy-pagination');
    const vacancyItems = Array.from(document.querySelectorAll('.vacancy-item'));

    let currentPage = 0;
    let filteredVacancies = [];

    if (!filterForm || vacancyItems.length === 0) {
        return;
    }

    function getFilterData() {
        return {
            text: filterText.value.trim().toLowerCase(),
            categoryId: filterCategory.value,
            experience: filterExperience.value,
            salaryFrom: filterSalaryFrom.value,
            salaryTo: filterSalaryTo.value
        };
    }

    function saveFilter(filterData) {
        localStorage.setItem(filterKey, JSON.stringify(filterData));
    }

    function restoreFilter() {
        const savedFilter = localStorage.getItem(filterKey);

        if (!savedFilter) {
            return;
        }

        const filterData = JSON.parse(savedFilter);

        filterText.value = filterData.text || '';
        filterCategory.value = filterData.categoryId || '';
        filterExperience.value = filterData.experience || '';
        filterSalaryFrom.value = filterData.salaryFrom || '';
        filterSalaryTo.value = filterData.salaryTo || '';
    }

    function vacancyMatchesFilter(vacancy, filterData) {
        const name = vacancy.dataset.name || '';
        const description = vacancy.dataset.description || '';
        const categoryId = vacancy.dataset.categoryId || '';
        const salary = Number(vacancy.dataset.salary || 0);
        const expFrom = Number(vacancy.dataset.expFrom || 0);
        const expTo = Number(vacancy.dataset.expTo || 0);

        if (filterData.text && !name.includes(filterData.text) && !description.includes(filterData.text)) {
            return false;
        }

        if (filterData.categoryId && categoryId !== filterData.categoryId) {
            return false;
        }

        if (filterData.salaryFrom && salary < Number(filterData.salaryFrom)) {
            return false;
        }

        if (filterData.salaryTo && salary > Number(filterData.salaryTo)) {
            return false;
        }

        if (filterData.experience) {
            const experience = Number(filterData.experience);

            if (experience < expFrom || experience > expTo) {
                return false;
            }
        }

        return true;
    }

    function hideAllVacancies() {
        vacancyItems.forEach(function (vacancy) {
            vacancy.classList.add('d-none');
        });
    }

    function renderVacanciesPage() {
        hideAllVacancies();

        if (filteredVacancies.length === 0) {
            emptyBlock.classList.remove('d-none');
            pagination.innerHTML = '';
            return;
        }

        emptyBlock.classList.add('d-none');

        const startIndex = currentPage * pageSize;
        const endIndex = startIndex + pageSize;
        const vacanciesForPage = filteredVacancies.slice(startIndex, endIndex);

        vacanciesForPage.forEach(function (vacancy) {
            vacancy.classList.remove('d-none');
        });

        renderPagination();
    }

    function renderPagination() {
        const totalPages = Math.ceil(filteredVacancies.length / pageSize);
        pagination.innerHTML = '';

        if (totalPages <= 1) {
            return;
        }

        for (let pageIndex = 0; pageIndex < totalPages; pageIndex++) {
            const item = document.createElement('li');
            item.className = 'page-item';

            if (pageIndex === currentPage) {
                item.classList.add('active');
            }

            const link = document.createElement('button');
            link.type = 'button';
            link.className = 'page-link';
            link.textContent = pageIndex + 1;

            link.addEventListener('click', function () {
                currentPage = pageIndex;
                renderVacanciesPage();
            });

            item.appendChild(link);
            pagination.appendChild(item);
        }
    }

    function applyFilter() {
        const filterData = getFilterData();

        filteredVacancies = vacancyItems.filter(function (vacancy) {
            return vacancyMatchesFilter(vacancy, filterData);
        });

        currentPage = 0;
        saveFilter(filterData);
        renderVacanciesPage();
    }

    function clearFilter() {
        localStorage.removeItem(filterKey);

        filterText.value = '';
        filterCategory.value = '';
        filterExperience.value = '';
        filterSalaryFrom.value = '';
        filterSalaryTo.value = '';

        filteredVacancies = vacancyItems;
        currentPage = 0;

        renderVacanciesPage();
    }

    applyButton.addEventListener('click', applyFilter);
    clearButton.addEventListener('click', clearFilter);

    restoreFilter();
    applyFilter();
});