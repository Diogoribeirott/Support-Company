package com.suport.api.controller;

import com.suport.api.domain.Address;
import com.suport.api.dtos.AddressDTO;
import com.suport.api.service.AddressService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
public class AddressController {

    private AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public  ResponseEntity<Address> save(@RequestBody @Valid AddressDTO body){
        return  ResponseEntity.status(HttpStatus.CREATED).body(addressService.save(body));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> findID(@PathVariable Long id){
         return ResponseEntity.ok().body(addressService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping
    public ResponseEntity<List<Address>> findAll(){
         return ResponseEntity.ok().body(addressService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> update(@RequestBody  @Valid AddressDTO body, @PathVariable Long id){
        return ResponseEntity.ok().body(addressService.update(body, id));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> delete(@PathVariable Long id){
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
