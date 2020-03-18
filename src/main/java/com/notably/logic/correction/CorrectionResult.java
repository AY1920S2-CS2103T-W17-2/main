package com.notably.logic.correction;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents the result of a correction.
 */
public class CorrectionResult<T> {
    private final CorrectionStatus correctionStatus;
    private final T correctedItem;

    /**
     * Creates a correction result without any corrected item.
     *
     * @param correctionStatus Status of the result
     */
    public CorrectionResult(CorrectionStatus correctionStatus) {
        this(correctionStatus, null);
    }

    /**
     * Creates a correction result.
     *
     * @param correctionStatus Status of the result
     * @param correctedItem Item after correction
     */
    public CorrectionResult(CorrectionStatus correctionStatus, T correctedItem) {
        Objects.requireNonNull(correctionStatus);

        this.correctionStatus = correctionStatus;
        this.correctedItem = correctedItem;
    }

    /**
     * Gets the status of the correction.
     *
     * @return Status of the correction
     */
    public CorrectionStatus getCorrectionStatus() {
        return correctionStatus;
    }

    /**
     * Gets the corrected item.
     * If no corrected item, {@code Optional.empty()} will be returned.
     *
     * @return Corrected item, or {@code Optional.empty()} if there is none
     */
    public Optional<T> getCorrectedItem() {
        return Optional.ofNullable(correctedItem);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CorrectionResult)) {
            return false;
        }

        CorrectionResult<?> another = (CorrectionResult<?>) object;
        return Objects.equals(correctionStatus, another.correctionStatus)
                && Objects.equals(correctedItem, another.correctedItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(correctionStatus, correctedItem);
    }
}

