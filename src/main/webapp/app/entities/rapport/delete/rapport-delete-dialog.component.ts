import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRapport } from '../rapport.model';
import { RapportService } from '../service/rapport.service';

@Component({
  standalone: true,
  templateUrl: './rapport-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RapportDeleteDialogComponent {
  rapport?: IRapport;

  constructor(
    protected rapportService: RapportService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rapportService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
