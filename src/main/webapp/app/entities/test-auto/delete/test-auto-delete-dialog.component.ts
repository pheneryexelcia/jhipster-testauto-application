import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITestAuto } from '../test-auto.model';
import { TestAutoService } from '../service/test-auto.service';

@Component({
  standalone: true,
  templateUrl: './test-auto-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TestAutoDeleteDialogComponent {
  testAuto?: ITestAuto;

  constructor(
    protected testAutoService: TestAutoService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.testAutoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
