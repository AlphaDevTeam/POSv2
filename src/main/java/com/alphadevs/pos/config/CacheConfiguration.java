package com.alphadevs.pos.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.alphadevs.pos.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.alphadevs.pos.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.alphadevs.pos.domain.User.class.getName());
            createCache(cm, com.alphadevs.pos.domain.Authority.class.getName());
            createCache(cm, com.alphadevs.pos.domain.User.class.getName() + ".authorities");
            createCache(cm, com.alphadevs.pos.domain.Products.class.getName());
            createCache(cm, com.alphadevs.pos.domain.Desings.class.getName());
            createCache(cm, com.alphadevs.pos.domain.Job.class.getName());
            createCache(cm, com.alphadevs.pos.domain.Job.class.getName() + ".details");
            createCache(cm, com.alphadevs.pos.domain.Job.class.getName() + ".assignedTos");
            createCache(cm, com.alphadevs.pos.domain.JobDetais.class.getName());
            createCache(cm, com.alphadevs.pos.domain.JobStatus.class.getName());
            createCache(cm, com.alphadevs.pos.domain.Items.class.getName());
            createCache(cm, com.alphadevs.pos.domain.ItemBinCard.class.getName());
            createCache(cm, com.alphadevs.pos.domain.PurchaseOrder.class.getName());
            createCache(cm, com.alphadevs.pos.domain.PurchaseOrder.class.getName() + ".details");
            createCache(cm, com.alphadevs.pos.domain.PurchaseOrderDetails.class.getName());
            createCache(cm, com.alphadevs.pos.domain.GoodsReceipt.class.getName());
            createCache(cm, com.alphadevs.pos.domain.GoodsReceipt.class.getName() + ".details");
            createCache(cm, com.alphadevs.pos.domain.GoodsReceipt.class.getName() + ".linkedPOs");
            createCache(cm, com.alphadevs.pos.domain.GoodsReceiptDetails.class.getName());
            createCache(cm, com.alphadevs.pos.domain.CashBook.class.getName());
            createCache(cm, com.alphadevs.pos.domain.CashBookBalance.class.getName());
            createCache(cm, com.alphadevs.pos.domain.CustomerAccount.class.getName());
            createCache(cm, com.alphadevs.pos.domain.CustomerAccountBalance.class.getName());
            createCache(cm, com.alphadevs.pos.domain.SupplierAccount.class.getName());
            createCache(cm, com.alphadevs.pos.domain.SupplierAccountBalance.class.getName());
            createCache(cm, com.alphadevs.pos.domain.PurchaseAccount.class.getName());
            createCache(cm, com.alphadevs.pos.domain.PurchaseAccountBalance.class.getName());
            createCache(cm, com.alphadevs.pos.domain.SalesAccount.class.getName());
            createCache(cm, com.alphadevs.pos.domain.SalesAccountBalance.class.getName());
            createCache(cm, com.alphadevs.pos.domain.DocumentType.class.getName());
            createCache(cm, com.alphadevs.pos.domain.TransactionType.class.getName());
            createCache(cm, com.alphadevs.pos.domain.Location.class.getName());
            createCache(cm, com.alphadevs.pos.domain.Location.class.getName() + ".users");
            createCache(cm, com.alphadevs.pos.domain.Customer.class.getName());
            createCache(cm, com.alphadevs.pos.domain.Supplier.class.getName());
            createCache(cm, com.alphadevs.pos.domain.Worker.class.getName());
            createCache(cm, com.alphadevs.pos.domain.ExUser.class.getName());
            createCache(cm, com.alphadevs.pos.domain.ExUser.class.getName() + ".locations");
            createCache(cm, com.alphadevs.pos.domain.Stock.class.getName());
            createCache(cm, com.alphadevs.pos.domain.Company.class.getName());
            createCache(cm, com.alphadevs.pos.domain.Designs.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }

}
