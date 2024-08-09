package com.matdang.seatdang.admin.service;

import com.matdang.seatdang.admin.dto.StoreRegistRequestDto;
import com.matdang.seatdang.admin.repository.StoreAdminRepository;
import com.matdang.seatdang.store.entity.Store;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreAdminService {
    private final StoreAdminRepository storeAdminRepository;

    public void regist(Store store) {
        storeAdminRepository.save(store);
    }

    public int findByStoreName(String storeName) {
        Store store = storeAdminRepository.findByStoreName(storeName);
        // Store 객체가 null이면 사용 가능(중복되지 않음), 그렇지 않으면 사용 중(중복됨)
        return (store == null) ? 0 : 1;
    }
}
