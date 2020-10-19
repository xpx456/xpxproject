package com.iccard;

public interface ICCardReaderObserver {
    // icCard : null -> 数据不完整
    void findCard(String id);
    void findICReader(Boolean isFound);
}
