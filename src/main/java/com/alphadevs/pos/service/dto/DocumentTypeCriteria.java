package com.alphadevs.pos.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.alphadevs.pos.domain.DocumentType} entity. This class is used
 * in {@link com.alphadevs.pos.web.rest.DocumentTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DocumentTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter documentTypeCode;

    private StringFilter documentType;

    public DocumentTypeCriteria(){
    }

    public DocumentTypeCriteria(DocumentTypeCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.documentTypeCode = other.documentTypeCode == null ? null : other.documentTypeCode.copy();
        this.documentType = other.documentType == null ? null : other.documentType.copy();
    }

    @Override
    public DocumentTypeCriteria copy() {
        return new DocumentTypeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(StringFilter documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    public StringFilter getDocumentType() {
        return documentType;
    }

    public void setDocumentType(StringFilter documentType) {
        this.documentType = documentType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DocumentTypeCriteria that = (DocumentTypeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(documentTypeCode, that.documentTypeCode) &&
            Objects.equals(documentType, that.documentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        documentTypeCode,
        documentType
        );
    }

    @Override
    public String toString() {
        return "DocumentTypeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (documentTypeCode != null ? "documentTypeCode=" + documentTypeCode + ", " : "") +
                (documentType != null ? "documentType=" + documentType + ", " : "") +
            "}";
    }

}
