import {Component, OnDestroy, OnInit} from '@angular/core';
import {MatBottomSheetRef} from '@angular/material';
import {Router} from '@angular/router';
import {TvShowService} from './tv-show.service';
import {ErrorService} from '../error/error.service';

@Component({
  selector: 'app-add-tv-show-popup',
  templateUrl: './add-tv-show-popup.component.html'
})
export class AddTvShowPopupComponent implements OnInit, OnDestroy {
  private tvShow = {};
  constructor(private tvShowService: TvShowService, private bottomSheet: MatBottomSheetRef<AddTvShowPopupComponent>,
              private router: Router, private errorService: ErrorService) {}
  async add() {
    try {
      const createdTvShow = await this.tvShowService.addTvShow(this.tvShow);
      this.router.navigateByUrl(`/tv-show/${createdTvShow.id}/thumbnail`);
    } catch (e) {
      this.errorService.notifyAbout(e, AddTvShowPopupComponent);
    } finally {
      this.bottomSheet.dismiss();
    }
  }
  ngOnDestroy(): void {
    this.errorService.unregisterFailingComponent(AddTvShowPopupComponent);
  }

  ngOnInit(): void {
    this.errorService.registerFailingComponent(AddTvShowPopupComponent);
  }
}
