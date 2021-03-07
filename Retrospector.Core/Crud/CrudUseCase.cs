using System;
using Retrospector.Core.Boundary;
using Retrospector.Core.Crud.Interfaces;
using Retrospector.Core.Crud.Models;

namespace Retrospector.Core.Crud
{
    public class CrudUseCase<T> : IUseCase<CrudRequest<T>> where T : IModel
    {
        private readonly ICrudPresenter<T> _presenter;
        private readonly ICrudDataGateway<T> _dataGateway;

        public CrudUseCase(ICrudPresenter<T> presenter, ICrudDataGateway<T> dataGateway)
        {
            _presenter = presenter;
            _dataGateway = dataGateway;
        }

        public void Execute(CrudRequest<T> request)
        {
            switch (request.Crud)
            {
                case CrudEnum.Create:
                    _presenter.Added(_dataGateway.Add(request.Model));
                    break;
                case CrudEnum.Read:
                    _presenter.Retrieved(_dataGateway.Get(request.ModelId));
                    break;
                case CrudEnum.Update:
                    _presenter.Updated(_dataGateway.Update(request.Model));
                    break;
                case CrudEnum.Delete:
                    _dataGateway.Delete(request.ModelId);
                    _presenter.Deleted(request.ModelId);
                    break;
                case CrudEnum.ReadAll:
                    _presenter.RetrievedAll(_dataGateway.GetAll());
                    break;
                case CrudEnum.ReadByMediaId:
                    _presenter.RetrievedByMediaId(_dataGateway.GetByMediaId(request.ModelId));
                    break;
                default:
                    throw new ArgumentOutOfRangeException();
            }
        }
    }
}