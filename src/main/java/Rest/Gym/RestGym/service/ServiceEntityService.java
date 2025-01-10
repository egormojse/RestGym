package Rest.Gym.RestGym.service;

import Rest.Gym.RestGym.dto.ServiceDTO;
import Rest.Gym.RestGym.model.Service;
import Rest.Gym.RestGym.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.management.ServiceNotFoundException;
import java.util.List;

@org.springframework.stereotype.Service
@Transactional
public class ServiceEntityService {
    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceEntityService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public Service createService(ServiceDTO serviceDTO) {
        Service service = new Service();
        updateServiceFromDTO(service, serviceDTO);
        return serviceRepository.save(service);
    }

    public Service getServiceById(int id) throws ServiceNotFoundException {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Услуга не найдена с ID: " + id));
    }

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public List<Service> getServicesByType(String type) {
        return serviceRepository.findByType(type);
    }

    public Service updateService(int id, ServiceDTO serviceDTO) throws ServiceNotFoundException {
        Service service = getServiceById(id);
        updateServiceFromDTO(service, serviceDTO);
        return serviceRepository.save(service);
    }

    public void deleteService(int id) throws ServiceNotFoundException {
        if (!serviceRepository.existsById(id)) {
            throw new ServiceNotFoundException("Услуга не найдена с ID: " + id);
        }
        serviceRepository.deleteById(id);
    }

    private void updateServiceFromDTO(Service service, ServiceDTO dto) {
        service.setName(dto.getName());
        service.setPrice(dto.getPrice());
        service.setType(dto.getType());
    }
}