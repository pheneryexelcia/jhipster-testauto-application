import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITestAuto, NewTestAuto } from '../test-auto.model';

export type PartialUpdateTestAuto = Partial<ITestAuto> & Pick<ITestAuto, 'id'>;

export type EntityResponseType = HttpResponse<ITestAuto>;
export type EntityArrayResponseType = HttpResponse<ITestAuto[]>;

@Injectable({ providedIn: 'root' })
export class TestAutoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/test-autos');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(testAuto: NewTestAuto): Observable<EntityResponseType> {
    return this.http.post<ITestAuto>(this.resourceUrl, testAuto, { observe: 'response' });
  }

  update(testAuto: ITestAuto): Observable<EntityResponseType> {
    return this.http.put<ITestAuto>(`${this.resourceUrl}/${this.getTestAutoIdentifier(testAuto)}`, testAuto, { observe: 'response' });
  }

  partialUpdate(testAuto: PartialUpdateTestAuto): Observable<EntityResponseType> {
    return this.http.patch<ITestAuto>(`${this.resourceUrl}/${this.getTestAutoIdentifier(testAuto)}`, testAuto, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITestAuto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITestAuto[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTestAutoIdentifier(testAuto: Pick<ITestAuto, 'id'>): number {
    return testAuto.id;
  }

  compareTestAuto(o1: Pick<ITestAuto, 'id'> | null, o2: Pick<ITestAuto, 'id'> | null): boolean {
    return o1 && o2 ? this.getTestAutoIdentifier(o1) === this.getTestAutoIdentifier(o2) : o1 === o2;
  }

  addTestAutoToCollectionIfMissing<Type extends Pick<ITestAuto, 'id'>>(
    testAutoCollection: Type[],
    ...testAutosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const testAutos: Type[] = testAutosToCheck.filter(isPresent);
    if (testAutos.length > 0) {
      const testAutoCollectionIdentifiers = testAutoCollection.map(testAutoItem => this.getTestAutoIdentifier(testAutoItem)!);
      const testAutosToAdd = testAutos.filter(testAutoItem => {
        const testAutoIdentifier = this.getTestAutoIdentifier(testAutoItem);
        if (testAutoCollectionIdentifiers.includes(testAutoIdentifier)) {
          return false;
        }
        testAutoCollectionIdentifiers.push(testAutoIdentifier);
        return true;
      });
      return [...testAutosToAdd, ...testAutoCollection];
    }
    return testAutoCollection;
  }
}
