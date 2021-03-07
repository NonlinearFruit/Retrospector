using Retrospector.Core.Boundary;
using Retrospector.Core.Crud;
using Retrospector.Core.Crud.Models;
using Retrospector.Core.Tests.TestDoubles.Crud;
using Retrospector.Core.Tests.TestDoubles.Crud.Models;
using Retrospector.Core.Tests.Utilities;
using Xunit;

namespace Retrospector.Core.Tests.Tests.Crud
{
    public class CrudUseCaseTests
    {
        private IUseCase<CrudRequest<Model_TestDouble>> _useCase;
        private CrudPresenter_TestDouble<Model_TestDouble> _presenter;
        private CrudDataGateway_TestDouble<Model_TestDouble> _dataGateway;

        public CrudUseCaseTests()
        {
            _presenter = new CrudPresenter_TestDouble<Model_TestDouble>();
            _dataGateway = new CrudDataGateway_TestDouble<Model_TestDouble>();
            _useCase = new CrudUseCase<Model_TestDouble>(_presenter, _dataGateway);
        }

        [Fact]
        public void create_request_calls_data_gateway()
        {
            var model = new Model_TestDouble();

            _useCase.Execute(new CrudRequest<Model_TestDouble>
            {
                Crud = CrudEnum.Create,
                Model = model
            });

            Assert.Equal(Verify.Once, _dataGateway.CountOf_Add_Calls);
            Assert.Equal(model, _dataGateway.LastModelPassedTo_Add);
        }

        [Fact]
        public void create_request_informs_presenter()
        {
            var model = new Model_TestDouble();
            _dataGateway.ReturnFor_Add = model;

            _useCase.Execute(new CrudRequest<Model_TestDouble>
            {
                Crud = CrudEnum.Create,
            });

            Assert.Equal(Verify.Once, _presenter.CountOf_Added_Calls);
            Assert.Equal(model, _presenter.LastModelPassedTo_Added);
        }

        [Fact]
        public void read_request_calls_data_gateway()
        {
            var id = 1245;

            _useCase.Execute(new CrudRequest<Model_TestDouble>
            {
                Crud = CrudEnum.Read,
                ModelId = id
            });

            Assert.Equal(Verify.Once, _dataGateway.CountOf_Get_Calls);
            Assert.Equal(id, _dataGateway.LastIdPassedTo_Get);
        }

        [Fact]
        public void read_request_informs_presenter()
        {
            var model = new Model_TestDouble();
            _dataGateway.ReturnFor_Get = model;

            _useCase.Execute(new CrudRequest<Model_TestDouble>
            {
                Crud = CrudEnum.Read
            });

            Assert.Equal(Verify.Once, _presenter.CountOf_Retrieved_Calls);
            Assert.Equal(model, _presenter.LastModelPassedTo_Retrieved);
        }

        [Fact]
        public void update_request_calls_data_gateway()
        {
            var model = new Model_TestDouble();

            _useCase.Execute(new CrudRequest<Model_TestDouble>
            {
                Crud = CrudEnum.Update,
                Model = model
            });

            Assert.Equal(Verify.Once, _dataGateway.CountOf_Update_Calls);
            Assert.Equal(model, _dataGateway.LastModelPassedTo_Update);
        }

        [Fact]
        public void update_request_informs_presenter()
        {
            var model = new Model_TestDouble();
            _dataGateway.ReturnFor_Update = model;

            _useCase.Execute(new CrudRequest<Model_TestDouble>
            {
                Crud = CrudEnum.Update
            });

            Assert.Equal(Verify.Once, _presenter.CountOf_Updated_Calls);
            Assert.Equal(model, _presenter.LastModelPassedTo_Updated);
        }

        [Fact]
        public void delete_request_calls_data_gateway()
        {
            var id = 250981;

            _useCase.Execute(new CrudRequest<Model_TestDouble>
            {
                Crud = CrudEnum.Delete,
                ModelId = id
            });

            Assert.Equal(Verify.Once, _dataGateway.CountOf_Delete_Calls);
            Assert.Equal(id, _dataGateway.LastIdPassedTo_Delete);
        }

        [Fact]
        public void delete_request_informs_presenter()
        {
            var id = 25;

            _useCase.Execute(new CrudRequest<Model_TestDouble>
            {
                Crud = CrudEnum.Delete,
                ModelId = id
            });

            Assert.Equal(Verify.Once, _presenter.CountOf_Deleted_Calls);
            Assert.Equal(id, _presenter.LastIdPassedTo_Deleted);
        }

        [Fact]
        public void read_all_request_calls_data_gateway()
        {
            _useCase.Execute(new CrudRequest<Model_TestDouble>
            {
                Crud = CrudEnum.ReadAll
            });

            Assert.Equal(Verify.Once, _dataGateway.CountOf_GetAll_Calls);
        }

        [Fact]
        public void read_all_request_informs_presenter()
        {
            var models = new Model_TestDouble[0];
            _dataGateway.ReturnFor_GetAll = models;

            _useCase.Execute(new CrudRequest<Model_TestDouble>
            {
                Crud = CrudEnum.ReadAll
            });

            Assert.Equal(Verify.Once, _presenter.CountOf_RetrievedAll_Calls);
            Assert.Equal(models, _presenter.LastModelsPassedTo_RetrievedAll);
        }

        [Fact]
        public void read_by_media_id_request_calls_data_gateway()
        {
            var id = 3152;

            _useCase.Execute(new CrudRequest<Model_TestDouble>
            {
                Crud = CrudEnum.ReadByMediaId,
                ModelId = id
            });

            Assert.Equal(Verify.Once, _dataGateway.CountOf_GetByMediaId_Calls);
            Assert.Equal(id, _dataGateway.LastMediaIdPassedTo_GetByMediaId);
        }

        [Fact]
        public void read_by_media_id_request_informs_presenter()
        {
            var models = new Model_TestDouble[0];
            _dataGateway.ReturnFor_GetMediaById = models;

            _useCase.Execute(new CrudRequest<Model_TestDouble>
            {
                Crud = CrudEnum.ReadByMediaId
            });

            Assert.Equal(Verify.Once, _presenter.CountOf_RetrievedByMediaId_Calls);
            Assert.Equal(models, _presenter.LastModelsPassedTo_RetrievedByMediaId);
        }

        [Theory]
        [InlineData(CrudEnum.Create)]
        [InlineData(CrudEnum.Delete)]
        [InlineData(CrudEnum.Read)]
        [InlineData(CrudEnum.Update)]
        [InlineData(CrudEnum.ReadAll)]
        [InlineData(CrudEnum.ReadByMediaId)]
        public void requests_only_call_data_gateway_once(CrudEnum crud)
        {
            _useCase.Execute(new CrudRequest<Model_TestDouble>
            {
                Crud = crud
            });

            var totalDataGatewayCalls =  _dataGateway.CountOf_GetAll_Calls +
                _dataGateway.CountOf_GetByMediaId_Calls +
                _dataGateway.CountOf_Add_Calls +
                _dataGateway.CountOf_Update_Calls +
                _dataGateway.CountOf_Delete_Calls +
                _dataGateway.CountOf_Get_Calls;
            Assert.Equal(Verify.Once, totalDataGatewayCalls);
        }

        [Theory]
        [InlineData(CrudEnum.Create)]
        [InlineData(CrudEnum.Delete)]
        [InlineData(CrudEnum.Read)]
        [InlineData(CrudEnum.Update)]
        [InlineData(CrudEnum.ReadAll)]
        [InlineData(CrudEnum.ReadByMediaId)]
        public void requests_only_call_presenter_once(CrudEnum crud)
        {
            _useCase.Execute(new CrudRequest<Model_TestDouble>
            {
                Crud = crud
            });

            var totalPresenterCalls =  _presenter.CountOf_Retrieved_Calls +
                _presenter.CountOf_Updated_Calls +
                _presenter.CountOf_Added_Calls +
                _presenter.CountOf_Deleted_Calls +
                _presenter.CountOf_RetrievedAll_Calls +
                _presenter.CountOf_RetrievedByMediaId_Calls;
            Assert.Equal(Verify.Once, totalPresenterCalls);
        }
    }
}