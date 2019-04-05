import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef} from '@angular/material';
import {TvShowService} from './tv-show.service';
import {ErrorService} from '../error/error.service';

@Component({
  selector: 'app-edit-tv-show',
  templateUrl: './edit-tv-show-popup.component.html'
})
export class EditTvShowPopupComponent implements OnInit, OnDestroy {
  private nameToDisplayOutside: string;
  constructor(@Inject(MAT_BOTTOM_SHEET_DATA) private data: any, private bottomSheet: MatBottomSheetRef<EditTvShowPopupComponent>,
              private tvShowService: TvShowService, private errorService: ErrorService) {}
  async save() {
    try {
      await this.tvShowService.updateTvShow(this.data.tvShow);
      this.nameToDisplayOutside = this.data.tvShow.name;
    } catch (e) {
      this.errorService.notifyAbout(e, EditTvShowPopupComponent);
    } finally {
      this.bottomSheet.dismiss();
    }
  }
  ngOnDestroy(): void {
    this.data.tvShow.name = this.nameToDisplayOutside;
    this.errorService.unregisterFailingComponent(EditTvShowPopupComponent);
  }
  ngOnInit(): void {
    this.nameToDisplayOutside = this.data.tvShow.name;
    this.errorService.registerFailingComponent(EditTvShowPopupComponent);
  }
}
