package com.yurets_y.payment_statistic_web.service.railroad_documents_services;

import com.yurets_y.payment_statistic_web.entity.AditionalVagonInfo;
import com.yurets_y.payment_statistic_web.entity.Vagon;
import com.yurets_y.payment_statistic_web.repo.VagonsRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class VagonService {

    private VagonsRepo vagonsRepo;

    public VagonService(VagonsRepo vagonsRepo) {
        this.vagonsRepo = vagonsRepo;
    }

    public Vagon saveVagon(Vagon vagon){
        return vagon;
    }

    public AditionalVagonInfo addVagonInfoToVagon(Long vagonId, AditionalVagonInfo vagonInfo){
        Vagon vagon = vagonsRepo.getOne(vagonId);
        AditionalVagonInfo info;
        if(vagon.getVagonInfo() == null){
            info = vagonInfo;
        }else{
            info = vagon.getVagonInfo();
            copyNonNullProperties(vagonInfo,info);
        }
        info.setVagon(vagon);
        vagon.setVagonInfo(info);
        vagonsRepo.save(vagon);
        return info;
    }

    private void copyNonNullProperties(Object source, Object destination){
        BeanUtils.copyProperties(source, destination,
                getNullPropertyNames(source));
    }

    private String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet();
        for(java.beans.PropertyDescriptor pd : pds) {
            //check if value of this property is null then add it to the collection
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}
