window.addEventListener('load', function () {
    const filterKey = 'vacancyFilter';

    const filterForm = document.getElementById('vacancy-filter-form');
    const filterText = document.getElementById('filterText');
    const filterCategory = document.getElementById('filterCategory');
    const filterExperience = document.getElementById('filterExperience');
    const filterSalaryFrom = document.getElementById('filterSalaryFrom');
    const filterSalaryTo = document.getElementById('filterSalaryTo');
    const applyButton = document.getElementById('applyVacancyFilter');
    const clearButton = document.getElementById('clearVacancyFilter');
    const emptyBlock = document.getElementById('vacancy-filter-empty');
    const vacancyItems = document.querySelectorAll('.vacancy-item');

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

    function applyFilter() {
        const filterData = getFilterData();
        let visibleCount = 0;

        vacancyItems.forEach(function (vacancy) {
            if (vacancyMatchesFilter(vacancy, filterData)) {
                vacancy.classList.remove('d-none');
                visibleCount++;
            } else {
                vacancy.classList.add('d-none');
            }
        });

        if (visibleCount === 0) {
            emptyBlock.classList.remove('d-none');
        } else {
            emptyBlock.classList.add('d-none');
        }

        saveFilter(filterData);
    }

    function clearFilter() {
        localStorage.removeItem(filterKey);

        filterText.value = '';
        filterCategory.value = '';
        filterExperience.value = '';
        filterSalaryFrom.value = '';
        filterSalaryTo.value = '';

        vacancyItems.forEach(function (vacancy) {
            vacancy.classList.remove('d-none');
        });

        emptyBlock.classList.add('d-none');
    }

    applyButton.addEventListener('click', applyFilter);
    clearButton.addEventListener('click', clearFilter);

    restoreFilter();
    applyFilter();
});