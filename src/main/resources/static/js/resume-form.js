window.addEventListener('load', function () {
    const resumeForm = document.getElementById('resume-form');
    const contactList = document.getElementById('contact-list');
    const educationList = document.getElementById('education-list');
    const workList = document.getElementById('work-list');

    const addContactButton = document.getElementById('add-contact');
    const addEducationButton = document.getElementById('add-education');
    const addWorkButton = document.getElementById('add-work');

    if (!resumeForm) {
        return;
    }

    function getContactOptionsHtml() {
        const firstSelect = document.querySelector('.contact-type-input');

        if (!firstSelect) {
            return `<option value="">${resumeForm.dataset.selectContactType}</option>`;
        }

        return firstSelect.innerHTML;
    }

    function reindexContacts() {
        const blocks = contactList.querySelectorAll('.contact-block');

        blocks.forEach(function (block, index) {
            const typeInput = block.querySelector('.contact-type-input');
            const valueInput = block.querySelector('.contact-value-input');

            if (typeInput) {
                typeInput.name = `contactInfos[${index}].typeId`;
            }

            if (valueInput) {
                valueInput.name = `contactInfos[${index}].contactValue`;
            }
        });
    }

    function reindexEducations() {
        const blocks = educationList.querySelectorAll('.education-block');

        blocks.forEach(function (block, index) {
            const institutionInput = block.querySelector('.education-institution-input');
            const programInput = block.querySelector('.education-program-input');
            const startDateInput = block.querySelector('.education-start-date-input');
            const endDateInput = block.querySelector('.education-end-date-input');
            const degreeInput = block.querySelector('.education-degree-input');

            if (institutionInput) {
                institutionInput.name = `educationInfos[${index}].institution`;
            }

            if (programInput) {
                programInput.name = `educationInfos[${index}].program`;
            }

            if (startDateInput) {
                startDateInput.name = `educationInfos[${index}].startDate`;
            }

            if (endDateInput) {
                endDateInput.name = `educationInfos[${index}].endDate`;
            }

            if (degreeInput) {
                degreeInput.name = `educationInfos[${index}].degree`;
            }
        });
    }

    function reindexWorks() {
        const blocks = workList.querySelectorAll('.work-block');

        blocks.forEach(function (block, index) {
            const yearsInput = block.querySelector('.work-years-input');
            const companyInput = block.querySelector('.work-company-input');
            const positionInput = block.querySelector('.work-position-input');
            const responsibilitiesInput = block.querySelector('.work-responsibilities-input');

            if (yearsInput) {
                yearsInput.name = `workExperienceInfos[${index}].years`;
            }

            if (companyInput) {
                companyInput.name = `workExperienceInfos[${index}].companyName`;
            }

            if (positionInput) {
                positionInput.name = `workExperienceInfos[${index}].position`;
            }

            if (responsibilitiesInput) {
                responsibilitiesInput.name = `workExperienceInfos[${index}].responsibilities`;
            }
        });
    }

    function reindexAll() {
        reindexContacts();
        reindexEducations();
        reindexWorks();
    }

    function addRemoveHandler(block) {
        const removeButton = block.querySelector('.remove-block');

        if (!removeButton) {
            return;
        }

        removeButton.addEventListener('click', function () {
            block.remove();
            reindexAll();
        });
    }

    function addRemoveHandlersToExistingBlocks() {
        const blocks = document.querySelectorAll('.contact-block, .education-block, .work-block');

        blocks.forEach(function (block) {
            addRemoveHandler(block);
        });
    }

    addContactButton.addEventListener('click', function () {
        const block = document.createElement('div');
        block.className = 'contact-block border rounded p-3 mb-3';

        block.innerHTML = `
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h6 class="mb-0">${resumeForm.dataset.contactBlock}</h6>
                <button type="button" class="btn btn-outline-danger btn-sm remove-block">
                    ${resumeForm.dataset.removeText}
                </button>
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.contactType}</label>
                <select class="form-select contact-type-input">
                    ${getContactOptionsHtml()}
                </select>
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.contactValue}</label>
                <input type="text" class="form-control contact-value-input">
            </div>
        `;

        contactList.appendChild(block);
        addRemoveHandler(block);
        reindexContacts();
    });

    addEducationButton.addEventListener('click', function () {
        const block = document.createElement('div');
        block.className = 'education-block border rounded p-3 mb-3';

        block.innerHTML = `
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h6 class="mb-0">${resumeForm.dataset.educationBlock}</h6>
                <button type="button" class="btn btn-outline-danger btn-sm remove-block">
                    ${resumeForm.dataset.removeText}
                </button>
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.educationInstitution}</label>
                <input type="text" class="form-control education-institution-input">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.educationProgram}</label>
                <input type="text" class="form-control education-program-input">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.educationStartDate}</label>
                <input type="date" class="form-control education-start-date-input">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.educationEndDate}</label>
                <input type="date" class="form-control education-end-date-input">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.educationDegree}</label>
                <input type="text" class="form-control education-degree-input">
            </div>
        `;

        educationList.appendChild(block);
        addRemoveHandler(block);
        reindexEducations();
    });

    addWorkButton.addEventListener('click', function () {
        const block = document.createElement('div');
        block.className = 'work-block border rounded p-3 mb-3';

        block.innerHTML = `
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h6 class="mb-0">${resumeForm.dataset.workBlock}</h6>
                <button type="button" class="btn btn-outline-danger btn-sm remove-block">
                    ${resumeForm.dataset.removeText}
                </button>
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.workYears}</label>
                <input type="number" class="form-control work-years-input">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.workCompany}</label>
                <input type="text" class="form-control work-company-input">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.workPosition}</label>
                <input type="text" class="form-control work-position-input">
            </div>

            <div class="mb-3">
                <label class="form-label">${resumeForm.dataset.workResponsibilities}</label>
                <textarea class="form-control work-responsibilities-input" rows="4"></textarea>
            </div>
        `;

        workList.appendChild(block);
        addRemoveHandler(block);
        reindexWorks();
    });

    addRemoveHandlersToExistingBlocks();
    reindexAll();
});