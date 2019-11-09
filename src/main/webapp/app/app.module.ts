import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { PoSv2SharedModule } from 'app/shared/shared.module';
import { PoSv2CoreModule } from 'app/core/core.module';
import { PoSv2AppRoutingModule } from './app-routing.module';
import { PoSv2HomeModule } from './home/home.module';
import { PoSv2EntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    PoSv2SharedModule,
    PoSv2CoreModule,
    PoSv2HomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    PoSv2EntityModule,
    PoSv2AppRoutingModule
  ],
  declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [JhiMainComponent]
})
export class PoSv2AppModule {}
