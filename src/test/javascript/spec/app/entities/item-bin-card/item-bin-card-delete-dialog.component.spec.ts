import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PoSv2TestModule } from '../../../test.module';
import { ItemBinCardDeleteDialogComponent } from 'app/entities/item-bin-card/item-bin-card-delete-dialog.component';
import { ItemBinCardService } from 'app/entities/item-bin-card/item-bin-card.service';

describe('Component Tests', () => {
  describe('ItemBinCard Management Delete Component', () => {
    let comp: ItemBinCardDeleteDialogComponent;
    let fixture: ComponentFixture<ItemBinCardDeleteDialogComponent>;
    let service: ItemBinCardService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PoSv2TestModule],
        declarations: [ItemBinCardDeleteDialogComponent]
      })
        .overrideTemplate(ItemBinCardDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ItemBinCardDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ItemBinCardService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
